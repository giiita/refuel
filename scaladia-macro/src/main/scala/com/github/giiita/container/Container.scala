package com.github.giiita.container

import com.github.giiita.injector.scope.InjectableScope

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

trait Container {
  def flush[T](value: T, priority: Int)(x: WeakTypeTag[T]): Unit

  def getBuffer: ListBuffer[InjectableScope[_]]
}
