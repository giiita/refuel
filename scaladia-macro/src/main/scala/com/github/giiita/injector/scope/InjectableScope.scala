package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

trait InjectableScope[T] {
  val value: T
  val priority: Int

  def acceptedClass[X](x: Class[X]): Boolean
  def acceptedInstance[X](x: Any):  Boolean
  def open: Boolean = false
  def isSameAs[X: WeakTypeTag]: Boolean
}
