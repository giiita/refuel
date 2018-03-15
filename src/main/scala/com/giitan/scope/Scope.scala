package com.giitan.scope

import com.giitan.box.Container
import com.giitan.container._

import scala.reflect.runtime.universe._

object Scope {
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
    *
    * @param cls Allowed object.
    * @tparam A Allowed type.
    * @return
    */
  def accept[A](cls: A): Scope[X] = {
    implicitly[Container].scoped(cls.getClass, tt)
    this
  }

  /**
    * Allow access to all types.
    * Default this.
    */
  def acceptedGlobal: Unit = implicitly[Container].scoped(tt)
}
