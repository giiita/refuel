package com.phylage.scaladia.container

import scala.reflect.runtime.universe._

case class ContainerIndexedKey(value: String)
object ContainerIndexedKey {
  def apply(value: WeakTypeTag[_]): ContainerIndexedKey = ContainerIndexedKey(value.tpe.toString)
}
