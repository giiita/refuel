package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

case class OpenScope[T](value: T, priority: Int, x: WeakTypeTag[T]) extends AbstractScope[T](x) {
  def acceptedClass[X](x: Class[X]): Boolean = false

  def acceptedInstance[X](x: Any): Boolean = false

  override def open: Boolean = true
}
