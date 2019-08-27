package com.phylage.scaladia.container

import com.phylage.scaladia.effect.{Effect, EffectLike, Effective}
import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.InjectionPool.InjectionApplyment
import com.phylage.scaladia.injector.scope.IndexedSymbol
import com.phylage.scaladia.runtime.{InjectionReflector, RuntimeAutoDIExtractor, RuntimeAutoInjectableSymbols}

import scala.reflect.runtime.universe

object RuntimeInjectionPool extends com.phylage.scaladia.injector.InjectionPool {

  /* An injectable buffer that has not yet been initialized */
  private[this] val buffer: RuntimeAutoInjectableSymbols = RuntimeAutoDIExtractor.run()

  /* Effective type symbol */
  private[this] lazy val EFFECTIVE_ANNO_TYPE = universe.weakTypeOf[Effective]

  /* Reflector */
  private[this] lazy val reflector = implicitly[InjectionReflector]

  /**
    * Return function of inject a activated effection.
    */
  private[this] val getEffect: Container => Set[EffectLike] = {
    { c: Container =>
      c.findEffect match {
        case x if x.nonEmpty => x
        case _ => collect[Effect]
          .apply(c)
          .filter(_.value.activate)
          .map(_.value)
      }
    }
  }

  /**
    * Get a list of injection-enabled declarations of any type
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](implicit wtt: universe.WeakTypeTag[T]): InjectionApplyment[T] = { c =>
    mayBeEffectiveApply[T, universe.ModuleSymbol](
      buffer.modules.collect {
        case x if x.typeSignature.<:<(universe.weakTypeTag[AutoInject[T]].tpe) =>
          x.annotations.find(_.tree.tpe.=:=(EFFECTIVE_ANNO_TYPE)).flatMap(_.tree.children.lastOption) -> x
      }
    )(reflector.reflectModule[T])(c) match {
      case x if x.nonEmpty => x
      case _ =>
        mayBeEffectiveApply[T, universe.ClassSymbol](
          buffer.classes.collect {
            case x if x.toType.<:<(universe.weakTypeTag[AutoInject[T]].tpe) =>
              x.annotations.find(_.tree.tpe.=:=(EFFECTIVE_ANNO_TYPE)).flatMap(_.tree.children.lastOption) -> x
          }
        )(reflector.reflectClass[T])(c)
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
  private[this] def mayBeEffectiveApply[T, S](v: Set[(Option[universe.Tree], S)])
                                                                   (f: Container => Set[S] => Set[IndexedSymbol[T]])
                                                                   (c: Container)
  : Set[IndexedSymbol[T]] = {
    v.partition(_._1.isEmpty) match {
      case (nonEffect, effect) if effect.isEmpty =>
        f(c)(nonEffect.map(_._2))
      case (nonEffect, effect) =>
        val activeEff = getEffect(c).map(_.getClass.getTypeName)
        f(c) {
          nonEffect.map(_._2) ++ {
            effect.withFilter(_._1.fold(false)(x => activeEff.contains(reflector.reflectClass(x.tpe).getTypeName))).map(_._2)
          }
        }
    }
  }
}
