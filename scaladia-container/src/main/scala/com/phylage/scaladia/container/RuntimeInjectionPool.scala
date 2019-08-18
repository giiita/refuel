package com.phylage.scaladia.container

import com.phylage.scaladia.effect.{Effect, EffectLike, Effective}
import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.InjectionPool.InjectionApplyment
import com.phylage.scaladia.injector.scope.InjectableScope
import com.phylage.scaladia.runtime.{InjectionReflector, RuntimeAutoDIExtractor}

import scala.reflect.runtime.universe

object RuntimeInjectionPool extends com.phylage.scaladia.injector.InjectionPool {

  /* An injectable buffer that has not yet been initialized */
  private[this] val buffer: Vector[universe.Symbol] = RuntimeAutoDIExtractor.run()

  /* Effective type symbol */
  private[this] lazy val EFFECTIVE_ANNO_TYPE = universe.weakTypeOf[Effective]

  /* Reflector */
  private[this] lazy val reflector = implicitly[InjectionReflector]

  /**
    * Return function of inject a activated effection.
    */
  private[this] val getEffect: Container => Option[EffectLike] = {
    { c: Container =>
      c.findEffect.orElse {
        collect[Effect]
          .apply(c)
          .filter(_.value.activate)
          .sortBy(_.priority)(Ordering.Int.reverse)
          .headOption
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
  def collect[T](implicit wtt: universe.WeakTypeTag[T]): InjectionApplyment[T] = {
    val hasTTypes = buffer.collect {
      case x if x.typeSignature.<:<(universe.weakTypeTag[AutoInject[T]].tpe) =>
        x.annotations.find(_.tree.tpe.=:=(EFFECTIVE_ANNO_TYPE)).flatMap(_.tree.children.lastOption) -> x.asModule
    }

    mayBeEffectiveCollection(hasTTypes)
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
  private[this] def mayBeEffectiveCollection[T: universe.WeakTypeTag](v: Vector[(Option[universe.Tree], universe.ModuleSymbol)])
                                                                     (c: Container)
  : Vector[InjectableScope[T]] = {
    v.flatMap(x => x._1.map(_ -> x._2)) match {
      case x if x.isEmpty => implicitly[InjectionReflector].reflect[T](c)(v.map(_._2))
      case candidates => getEffect(c).map(_.getClass.getTypeName) match {
        case None => Vector.empty
        case Some(activeEff) =>
          reflector.reflect[T](c)(
            candidates.collect {
              case x if reflector.reflectClass(x._1.tpe).getTypeName == activeEff => x._2
            }
          )
      }
    }
  }
}
