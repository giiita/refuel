package com.phylage.scaladia.container

import com.phylage.scaladia.injector.InjectionType

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

object InjectionPool extends com.phylage.scaladia.injector.InjectionPool {
  private[this] val buffer: ListBuffer[InjectionType[_]] = ListBuffer()

  /**
    * Pool Injectable subtypes for automatic loading.
    * The timing to be initialized is when the related
    * component is initialized or when it is called by inject.
    *
    * @param applyer injection object
    * @return
    */
  def pool(applyer: () => Iterable[InjectionType[_]]): Unit = {
    applyer().collect {
      case x if !buffer.exists(_ =:= x) => buffer += x
    }
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
      } map(_.applyment)
    }.toVector.asInstanceOf[Vector[InjectionApplyment[T]]]
  }

  /**
    * Get a list of injection-enabled declarations of all
    *
    * @return
    */
  override def all(): Vector[InjectionPool.InjectionApplyment[_]] = {
    buffer.map(_.applyment).toVector
  }
}
