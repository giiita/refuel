package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

case class AcceptedFromInstanceScope[T](value: T, priority: Int = Int.MaxValue, x: WeakTypeTag[T], acceptedFrom: Vector[Any]) extends AbstractScope[T](x) {
  def acceptedClass[X](x: Class[X]): Boolean = false

  def acceptedInstance[X](x: Any): Boolean = acceptedFrom.contains(x)
}
