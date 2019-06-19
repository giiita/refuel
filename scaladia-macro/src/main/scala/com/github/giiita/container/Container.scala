package com.github.giiita.container

import com.github.giiita.injector.scope.InjectableScope

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

trait Container {
  def cache[T](value: T, priority: Int)(x: WeakTypeTag[T]): InjectableScope[T]

  def getBuffer: ListBuffer[InjectableScope[_]]
}
