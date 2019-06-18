package com.github.giiita.container

import com.github.giiita.injector.ContainerMacro

import scala.collection.mutable.ListBuffer

object Container {
  import scala.language.implicitConversions
  implicit def flush[T](value: T): T = {
    ContainerMacro.export(value)
  }
}
trait Container {



  val buffer: ListBuffer[Any] = ListBuffer.empty

  // def pickup[T](symbol: T): Unit = macro pickup_impl[T]

//  def get[T](): T = buffer.collectFirst {
//    case x: T =>
//      println(s"FOUND ${x.toString}")
//      x
//  }.get

  def get[T](): T = ContainerMacro.pickup[T]

  implicit def flush[T](value: T): T = {
    ContainerMacro.export(value)
  }

  case class Provider[T](value: T)
}
