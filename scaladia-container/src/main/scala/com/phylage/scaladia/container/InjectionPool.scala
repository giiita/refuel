package com.phylage.scaladia.container

import com.phylage.scaladia.injector.{InjectionPool, InjectionType}

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

object InjectionPool extends InjectionPool {
  private[this] val buffer: ListBuffer[InjectionType[_]] = ListBuffer()

  /**
    * Pool Injectable subtypes for automatic loading.
    * The timing to be initialized is when the related
    * component is initialized or when it is called by inject.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  def pool[T](value: InjectionType[T]): Unit = {
    if (!buffer.contains(value)) buffer += value
  }

  /**
    * Get a list of injection-enabled declarations of any type
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](implicit wtt: WeakTypeTag[T]): Vector[InjectionApplyment[T]] = {
    {
      buffer.collect {
        case x if wtt.tpe.=:=(x.wtt.tpe) => x
      } map {_.applyment}
    }.toVector.asInstanceOf[Vector[InjectionApplyment[T]]]
  }
}
