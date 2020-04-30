package refuel.container

import refuel.container.anno.Effective
import refuel.domination.InjectionPriority.Default
import refuel.domination.{Inject, InjectionPriority}
import refuel.effect.EffectLike
import refuel.exception.InjectDefinitionException
import refuel.injector.InjectionPool.LazyConstruction
import refuel.injector.scope.IndexedSymbol
import refuel.internal.di.Effect
import refuel.runtime.{InjectionReflector, RuntimeAutoDIExtractor, RuntimeAutoInjectableSymbols}

import scala.reflect.runtime.{universe => u}

object RuntimeInjectionPool extends refuel.injector.InjectionPool {

  /* Effective type symbol */
  private[this] lazy val EFFECTIVE_ANNO_TYPE = u.weakTypeOf[Effective]
  /* Injection priority config type tag */
  private[this] lazy val InjectionPriorityConfigType = u.weakTypeOf[Inject]
  /* Reflector */
  private[this] lazy val reflector = implicitly[InjectionReflector]
  /* An injectable buffer that has not yet been initialized */
  private[this] lazy val buffer: RuntimeAutoInjectableSymbols = RuntimeAutoDIExtractor.run()

  /**
    * Return function of inject activated effects.
    * Regardless of the injection request, all valid effects are returned.
    * Once acquired, the effect is indexed to the container as an injectable object.
    */
  private[this] val activeEffects: Container => Seq[EffectLike] = {
    { c: Container =>
      c.findEffect match {
        case x if x.nonEmpty => x.toSeq
        case _ =>
          collect[Effect](classOf[Effect])
            .apply(c)
            .fold[Seq[IndexedSymbol[Effect]]](Nil) {
              case (p, fs) =>
                fs.map { x =>
                  val s = x.apply(p)
                  c.cache(s)
                }
            }
            .withFilter(_.value.activate)
            .map(_.value)
      }
    }
  }

  private[this] def extractInjectionPriorityWithDefault(x: u.Symbol): InjectionPriority = {
    x.annotations
      .find(_.tree.tpe =:= InjectionPriorityConfigType)
      .flatMap(_.tree.children.tail.headOption)
      .fold[InjectionPriority](Default) {
        case x if x.symbol.isModule => reflector.embody[InjectionPriority](x.symbol.asModule)
        case _                      => throw new InjectDefinitionException("The injection priority setting must be a static module.")
      }
  }

  private[this] def mayBeExtractEffectType(x: u.Symbol): Option[u.Tree] = {
    x.annotations.find(_.tree.tpe.=:=(EFFECTIVE_ANNO_TYPE)).flatMap(_.tree.children.lastOption)
  }

  private[this] type InjectRankingSymbol[S <: u.Symbol] = (InjectionPriority, Option[u.Tree], S)

  private[this] def extractInjectRankingSymbol[S <: u.Symbol](x: S): InjectRankingSymbol[S] = {
    (
      extractInjectionPriorityWithDefault(x),
      mayBeExtractEffectType(x),
      x
    )
  }

  /**
    * Get a list of injection-enabled declarations of any type.
    * Next to ModuleSymbol, reflect ClassSymbol.
    * A class / object with an effective annotation will be indexed into the container if it is an effective effect.
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](clazz: Class[_])(implicit wtt: u.WeakTypeTag[T]): Container => Option[LazyConstruction[T]] = { c =>
    val injectionTypeSym = wtt.tpe

    val result = {
      mayBeEffectiveApply[T, u.ModuleSymbol](
        buffer.modules.collect {
          case x if x.typeSignature.<:<(injectionTypeSym) => extractInjectRankingSymbol(x)
        }
      )(reflector.reflectModule[T](c))(c) ++ mayBeEffectiveApply[T, u.ClassSymbol](
        buffer.classes.collect {
          case x if x.toType.<:<(injectionTypeSym) => extractInjectRankingSymbol(x)
        }
      )(reflector.reflectClass[T](clazz, this)(c))(c)
    }

    result.groupBy(_._1).toSeq.sortBy(_._1)(InjectionPriority.Order).headOption.map {
      case (p, fs) => p -> fs.map(_._2)
    }
  }

  /**
    * Returns a ModuleSymbol that matches the requested Type.
    *
    * If at least one of the auto injectable modules is Effective,
    * the activated Effect is loaded and only the module where that Effect is declared is returned.
    *
    * @see [[Effect]]
    * @param v May be effective annotation -> Auto scanning module.
    * @tparam T Requested type.
    * @return
    */
  private[this] def mayBeEffectiveApply[T, S <: u.Symbol](v: Set[InjectRankingSymbol[S]])(
      f: S => InjectionPriority => IndexedSymbol[T]
  )(c: Container): Seq[(InjectionPriority, InjectionPriority => IndexedSymbol[T])] = {
    v.partition(_._2.isEmpty) match {
      case (uneffected, effected) if effected.isEmpty =>
        uneffected.map {
          case (priority, _, sym) => priority -> f(sym)
        }
      case (uneffected, effected) =>
        val activeEff = activeEffects(c).map(_.getClass.getTypeName)
        uneffected.map {
          case (priority, _, sym) => priority -> f(sym)
        } ++ effected.collect {
          case (priority, mayBeEff, sym)
              if mayBeEff.fold(false)(x => activeEff.contains(reflector.reflectClass(x.tpe).getTypeName)) =>
            priority -> f(sym)
        }
    }
  }.toSeq
}
