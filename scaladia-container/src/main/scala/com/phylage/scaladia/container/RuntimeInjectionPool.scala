package com.phylage.scaladia.container

import com.phylage.scaladia.injector.{AutoInject, InjectionType}
import com.phylage.scaladia.runtime.{InjectionReflector, RuntimeAutoDIExtractor}

import scala.reflect.runtime.universe

object RuntimeInjectionPool extends com.phylage.scaladia.injector.InjectionPool {

  private[this] val buffer: Vector[universe.Symbol] = RuntimeAutoDIExtractor.run()

  /**
    * Pool Injectable subtypes for automatic loading.
    * The timing to be initialized is when the related
    * component is initialized or when it is called by inject.
    *
    * @param applyer injection object
    * @return
    */
  def pool(applyer: () => Iterable[InjectionType[_]]): Unit = {

  }

  /**
    * Get a list of injection-enabled declarations of any type
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](implicit wtt: universe.WeakTypeTag[T]): Vector[InjectionApplyment[T]] = {
    buffer.collect {
      case x if x.typeSignature.<:<(universe.weakTypeTag[AutoInject[T]].tpe) =>
        implicitly[InjectionReflector].reflect[T](x.asModule)
    }
  }
}
