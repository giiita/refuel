package com.github.giiita.container

import com.github.giiita.injector.scope.{OpenScope, Scope}

import scala.collection.mutable.ListBuffer

object Container {

  val buffer: ListBuffer[Scope[_]] = ListBuffer.empty

  def flush[T](value: T): Unit = buffer.addOne(OpenScope[T](value))

  def get[T, X](from: X): T = buffer.collectFirst {
    case x: Scope[T] if x.accept(from) => x.value
  } orElse buffer.collectFirst {
      case x: Scope[T] if x.accept[X] => x.value
    } orElse buffer.collectFirst {
    case x: Scope[T] if x.open => x.value
  } getOrElse {
    throw new Exception("Dependencies not found.")
  }
}
