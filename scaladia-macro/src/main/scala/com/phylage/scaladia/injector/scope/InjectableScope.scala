package com.phylage.scaladia.injector.scope

import com.phylage.scaladia.provider.Accessor

import scala.reflect.runtime.universe._

/**
  * This is a globally held injectable object.
  * Injects the highest priority one of the scopes permitted from any scope.
  *
  * @tparam T injectable object type
  */
trait InjectableScope[T] {
  val value: T
  val priority: Int
  val tag: WeakTypeTag[T]

  /**
    * Determines if this object can be injected from any accessor.
    *
    * @param requestFrom Accessor of request source.
    * @return
    */
  final def accepted(requestFrom: Accessor[_]): Boolean = {
    isOpen || acceptedClass(requestFrom.t.getClass) || acceptedInstance(requestFrom.t)
  }

  /**
    * Returns true if there is an unscoped dependency.
    *
    * @return
    */
  protected def isOpen: Boolean = false

  /**
    * When permitting access from any class, it returns true if the class of the request source matches.
    *
    * @param x class of the request source
    * @tparam X class of the request source
    * @return
    */
  protected def acceptedClass[X](x: Class[X]): Boolean

  /**
    * When access from any instance is permitted, it returns true if the request source instance matches.
    *
    * @param x request source instance
    * @return
    */
  protected def acceptedInstance(x: Any): Boolean
}
