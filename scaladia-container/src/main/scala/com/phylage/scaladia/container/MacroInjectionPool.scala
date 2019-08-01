package com.phylage.scaladia.container

import com.phylage.scaladia.injector.InjectionType

import scala.collection.mutable.ListBuffer

object MacroInjectionPool extends RuntimeInjectionPool {
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
}
