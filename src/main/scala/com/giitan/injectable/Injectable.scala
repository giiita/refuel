package com.giitan.injectable

import scala.reflect.runtime.universe._

trait Injectable[T] {

  // Registed inject type.
  val tipe: Type
  // Injection value.
  val applier: T
  // Accessible type.
  var scope: Seq[Class[_]]

  /**
    *  Append accessible type.
    */
  def +=(c: Class[_]): Injectable[T]

  /**
    * Clear accessible type.
    */
  def clear: Unit
}
