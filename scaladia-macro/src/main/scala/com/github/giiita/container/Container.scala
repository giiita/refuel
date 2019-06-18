package com.github.giiita.container

import scala.collection.mutable.ListBuffer

object Container {

  val buffer: ListBuffer[Any] = ListBuffer.empty

  // def pickup[T](symbol: T): Unit = macro pickup_impl[T]

  def get[T](): T = buffer.collectFirst {
    case x: T =>
      println(s"FOUND ${x.toString}")
      x
  }.get

  def flush[T](value: T): Unit = buffer.addOne(value)

  case class Provider[T](value: T)
}
