package com.giitan.scope

import com.giitan.box.Container
import com.giitan.container._

import scala.reflect._
import runtime.universe._

object Scope {
  type ScopeType = Class[_]

  def apply[X: TypeTag]: Scope[X] = new Scope[X] {
    def tt: Type = typeOf[X]
  }
}

trait Scope[X] {
  /**
    * Super class of injected object
    *
    * @return
    */
  def tt: Type

  /**
    * Extend the allowed type.
    * Only that dynamic class.
    *
    * @param cls Allowed object.
    * @tparam A Allowed type.
    * @return
    */
  def accept[A: TypeTag](cls: A): Scope[X] = {
    implicitly[Container].appendScope(cls.getClass, tt)
    this
  }

  /**
    * Extend the allowed type.
    * Only that type.
    *
    * @tparam A Allowed type.
    * @return
    */
  def accept[A: ClassTag]: Scope[X] = {
    implicitly[Container].appendScope(classTag[A].runtimeClass, tt)
    this
  }

  /**
    * Allow access to all types.
    * Default this.
    */
  def acceptedGlobal(): Unit = implicitly[Container].globaly(tt)
}
