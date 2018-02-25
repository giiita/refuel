package com.giitan.scope

import com.giitan.box.Container
import com.giitan.container._

import scala.reflect.runtime.universe._

object Closed {
  def apply[X: TypeTag]: Closed[X] = new Closed[X] {
    def tt: Type = typeOf[X]
  }
}

trait Closed[X] extends Scope {
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
  def accept[A](cls: A): Closed[X] = {
    implicitly[Container].scoped(cls.getClass, tt)
    this
  }

  /**
    * Allow access to all types.
    */
  def acceptedGlobal: Unit = implicitly[Container].scoped(tt)
}
