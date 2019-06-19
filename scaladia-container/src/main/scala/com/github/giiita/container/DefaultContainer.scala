package com.github.giiita.container

import com.github.giiita.injector.RecoveredInject
import com.github.giiita.injector.scope.{InjectableScope, OpenScope}

import scala.collection.mutable.ListBuffer

object DefaultContainer extends Container with RecoveredInject[Container] {

  val buffer: ListBuffer[InjectableScope[_]] = ListBuffer()

  import scala.reflect.runtime.universe._

  def flush[T](value: T, priority: Int)(x: WeakTypeTag[T]): Unit = {
    println(s"flushing ${x.tpe}")
    buffer += OpenScope[T](value, priority, x)
  }

  def getBuffer: ListBuffer[InjectableScope[_]] = buffer

  override def flush[N <: Container](implicit x: WeakTypeTag[N]): Unit = {
    buffer += OpenScope(this, 0, x.asInstanceOf[WeakTypeTag[Container]])
  }
}
