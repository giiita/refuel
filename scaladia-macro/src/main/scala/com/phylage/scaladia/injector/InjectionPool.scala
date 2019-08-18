package com.phylage.scaladia.injector

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.InjectionPool.InjectionApplyment
import com.phylage.scaladia.injector.scope.InjectableScope

import scala.reflect.runtime.universe._

object InjectionPool {
  type InjectionApplyment[T] = Container => Vector[InjectableScope[T]]
}

trait InjectionPool {

  /**
    * Get a list of injection-enabled declarations of any type
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](implicit wtt: WeakTypeTag[T]): InjectionApplyment[T]
}
