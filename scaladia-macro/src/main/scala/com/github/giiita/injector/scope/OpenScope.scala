package com.github.giiita.injector.scope

case class OpenScope[T](value: T) extends Scope[T] {
  override def accept[X]: X => Boolean = _ => false
  override def accept(x: Any): Any => Boolean = _ => false
  override def open: Boolean = true
}
