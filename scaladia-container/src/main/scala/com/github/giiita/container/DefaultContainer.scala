package com.github.giiita.container

import com.github.giiita.injector.scope.{InjectableScope, OpenScope}

import scala.collection.mutable.ListBuffer

object DefaultContainer extends StorePublisherContainer {

  val buffer: ListBuffer[InjectableScope[_]] = ListBuffer()

  import scala.reflect.runtime.universe._

  def cache[T](value: T, priority: Int)(x: WeakTypeTag[T]): InjectableScope[T] = {
    println(s"flushing ${x.tpe}")
    OpenScope[T](value, priority, x) match {
      case sc if buffer.contains(sc) => sc
      case sc =>
        buffer += sc
        sc
    }
  }

  def getBuffer: ListBuffer[InjectableScope[_]] = buffer
}
