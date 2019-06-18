package com.github.giiita.injector

trait Provider[T] {
  val t: Option[T] = None
}
