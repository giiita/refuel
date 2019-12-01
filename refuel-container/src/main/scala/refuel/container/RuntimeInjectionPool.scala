package refuel.container

import refuel.effect.{Effect, EffectLike, Effective}
import refuel.injector.AutoInject
import refuel.injector.InjectionPool.InjectionApplyment
import refuel.injector.scope.IndexedSymbol
import refuel.runtime.{InjectionReflector, RuntimeAutoDIExtractor, RuntimeAutoInjectableSymbols}

import scala.reflect.runtime.{universe => u}

object RuntimeInjectionPool extends refuel.injector.InjectionPool {

  /* Effective type symbol */
  private[this] lazy val EFFECTIVE_ANNO_TYPE = u.weakTypeOf[Effective]
  /* Reflector */
  private[this] lazy val reflector = implicitly[InjectionReflector]
  /* An injectable buffer that has not yet been initialized */
  private[this] val buffer: RuntimeAutoInjectableSymbols = RuntimeAutoDIExtractor.run()
  /**
    * Return function of inject activated effects.
    * Regardless of the injection request, all valid effects are returned.
    * Once acquired, the effect is indexed to the container as an injectable object.
    */
  private[this] val getEffect: Container => Set[EffectLike] = {
    { c: Container =>
      c.findEffect match {
        case x if x.nonEmpty => x
        case _               => collect[Effect]
          .apply(c)
          .filter(_.value.activate)
          .map(_.value)
      }
    }
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
  def collect[T](implicit wtt: u.WeakTypeTag[T]): InjectionApplyment[T] = { c =>
    mayBeEffectiveApply[T, u.ModuleSymbol](
      buffer.modules.collect {
        case x if x.typeSignature.<:<(u.weakTypeTag[AutoInject[T]].tpe) =>
          x.annotations.find(_.tree.tpe.=:=(EFFECTIVE_ANNO_TYPE)).flatMap(_.tree.children.lastOption) -> x
      }
    )(reflector.reflectModule[T])(c) match {
      case x if x.nonEmpty => x
      case _               =>
        mayBeEffectiveApply[T, u.ClassSymbol](
          buffer.classes.collect {
            case x if x.toType.<:<(u.weakTypeTag[AutoInject[T]].tpe) =>
              x.annotations.find(_.tree.tpe.=:=(EFFECTIVE_ANNO_TYPE)).flatMap(_.tree.children.lastOption) -> x
          }
        )(reflector.reflectClass[T](this))(c)
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
    * @param c Container instance
    * @tparam T Requested type.
    * @return
    */
  private[this] def mayBeEffectiveApply[T, S](v: Set[(Option[u.Tree], S)])
                                             (f: Container => Set[S] => Set[IndexedSymbol[T]])
                                             (c: Container)
  : Set[IndexedSymbol[T]] = {
    v.partition(_._1.isEmpty) match {
      case (nonEffect, effect) if effect.isEmpty =>
        f(c)(nonEffect.map(_._2))
      case (nonEffect, effect)                   =>
        val activeEff = getEffect(c).map(_.getClass.getTypeName)
        f(c) {
          nonEffect.map(_._2) ++ {
            effect.withFilter(_._1.fold(false)(x => activeEff.contains(reflector.reflectClass(x.tpe).getTypeName))).map(_._2)
          }
        }
    }
  }
}
