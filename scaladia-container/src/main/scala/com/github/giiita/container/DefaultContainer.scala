package com.github.giiita.container

import com.github.giiita.`macro`.PickupMacro
import com.github.giiita.injector.RecoveredInject
import com.github.giiita.injector.scope.{InjectableScope, OpenScope}

import scala.collection.mutable.ListBuffer

object DefaultContainer extends Container with RecoveredInject[Container] {

  implicit val buffer: ListBuffer[InjectableScope[_]] = ListBuffer.empty

  import scala.reflect.runtime.universe._

  def flush[T](value: T, priority: Int)(x: WeakTypeTag[T]): Unit = {
    println(s"flushing ${x.tpe}")
    buffer += OpenScope[T](value, priority, x)
  }

  def getBuffer: ListBuffer[InjectableScope[_]] = buffer

  def get[T]: T = macro PickupMacro.pickup_impl[T]
}
