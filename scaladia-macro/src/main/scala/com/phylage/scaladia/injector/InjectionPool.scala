package com.phylage.scaladia.injector

import com.phylage.scaladia.injector.scope.InjectableScope

import scala.reflect.runtime.universe._

trait InjectionPool {
  type InjectionApplyment[T] = () => InjectableScope[T]

  /**
    * Pool Injectable subtypes for automatic loading.
    * The timing to be initialized is when the related
    * component is initialized or when it is called by inject.
    *
    * @param applyer injection object
    * @return
    */
  def pool(applyer: () => Iterable[InjectionType[_]]): Unit

  /**
    * Get a list of injection-enabled declarations of any type
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](implicit wtt: WeakTypeTag[T]): Vector[InjectionApplyment[T]]
}
