package com.github.giiita.injector.scope

trait Scope[T] {
  val value: T

  def accept[X]: X => Boolean
  def accept(x: Any): Any => Boolean
  def open: Boolean = false
}

object Scope {
  def unapply[T](arg: Scope[T]): Option[T] = Option(arg.value)
}
