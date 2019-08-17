package com.phylage.scaladia.container

import com.phylage.scaladia.injector.AutoInject
import com.phylage.scaladia.injector.InjectionPool.InjectionApplyment
import com.phylage.scaladia.runtime.{InjectionReflector, RuntimeAutoDIExtractor}

import scala.reflect.runtime.universe

object RuntimeInjectionPool extends com.phylage.scaladia.injector.InjectionPool {

  private[this] val buffer: Vector[universe.Symbol] = RuntimeAutoDIExtractor.run()


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
