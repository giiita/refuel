package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

case class AcceptedFromTypeScope[T](value: T, priority: Int = Int.MaxValue, x: WeakTypeTag[T], acceptedFrom: Vector[Class[_]]) extends AbstractScope[T](x) {
  def acceptedClass[X](x: Class[X]): Boolean = acceptedFrom.exists(_.isAssignableFrom(x))

  def acceptedInstance[X](x: Any): Boolean = false
}
