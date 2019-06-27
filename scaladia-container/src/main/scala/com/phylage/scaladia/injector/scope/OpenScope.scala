package com.phylage.scaladia.injector.scope

import scala.reflect.runtime.universe._

case class OpenScope[T](value: T, priority: Int, x: WeakTypeTag[T]) extends AbstractScope[T](x) {
  /**
    * When permitting access from any class, it returns true if the class of the request source matches.
    *
    * @param x class of the request source
    * @tparam X class of the request source
    * @return
    */
  def acceptedClass[X](x: Class[X]): Boolean = false

  /**
    * When access from any instance is permitted, it returns true if the request source instance matches.
    *
    * @param x request source instance
    * @return
    */
  def acceptedInstance(x: Any): Boolean = false

  /**
    * Returns true if there is an unscoped dependency.
    *
    * @return
    */
  override def isOpen: Boolean = true
}
