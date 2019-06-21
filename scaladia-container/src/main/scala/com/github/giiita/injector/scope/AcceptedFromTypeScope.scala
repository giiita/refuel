package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

/**
  * Class-Tolerant Scope Object
  *
  * @param value        Injection object
  * @param priority     priority
  * @param x            Injected object WeakTypeTag
  * @param acceptedFrom accepted type list
  * @tparam T Injection object type
  */
case class AcceptedFromTypeScope[T](value: T, priority: Int = Int.MaxValue, x: WeakTypeTag[T], acceptedFrom: Vector[Class[_]]) extends AbstractScope[T](x) {
  /**
    * When permitting access from any class, it returns true if the class of the request source matches.
    *
    * @param x class of the request source
    * @tparam X class of the request source
    * @return
    */
  def acceptedClass[X](x: Class[X]): Boolean = acceptedFrom.exists(_.isAssignableFrom(x))

  /**
    * When access from any instance is permitted, it returns true if the request source instance matches.
    *
    * @param x request source instance
    * @return
    */
  def acceptedInstance(x: Any): Boolean = false
}
