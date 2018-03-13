package com.giitan.injectable

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

trait Injectable[T] {

  // Registed inject type.
  val tipe: Type
  // Injection value.
  val applier: T
  // Accessible type.
  val scope: ListBuffer[Class[_]] = ListBuffer.empty

  /**
    *  Append accessible type.
    */
  def +=(c: Class[_]): Injectable[T]

  /**
    * Clear accessible type.
    */
  def clear: Unit
}
