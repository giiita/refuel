package com.giitan.injectable

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

trait Injectable[T] {

  // Registed inject type.
  val tipe: Type
  // Injection value.
  val applier: T
  // Accessible type.
  val scope: ListBuffer[Class[_]]

  /**
    *  Append accessible type.
    */
  def +=(c: Class[_]): Injectable[T] = {
    scope += c
    this
  }

  /**
    * Clear accessible type.
    */
  def clear: Unit = this.scope.clear()
}
