package refuel.injector.scope

import refuel.container.Container
import refuel.domination.InjectionPriority

import scala.reflect.runtime.universe._

/**
  * This is a globally held injectable object.
  * Injects the highest priority one of the scopes permitted from any scope.
  *
  * @tparam T injectable object type
  */
trait IndexedSymbol[T] {
  val key: scala.Symbol
  val value: T
  val priority: InjectionPriority
  val tag: Type
  val c: Container

  /**
    * Determines if this object can be injected from any accessor.
    *
    * @param requestFrom Accessor of request source.
    * @return
    */
  final def accepted[X: TypedAcceptContext](requestFrom: X): Boolean = {
    implicitly[TypedAcceptContext[X]].accepted(this)(requestFrom)
  }

  /**
    * Returns true if there is an unscoped dependency.
    *
    * @return
    */
  private[refuel] def isOpen: Boolean = false

  /**
    * When permitting access from any class, it returns true if the class of the request source matches.
    *
    * @param x class of the request source
    * @tparam X class of the request source
    * @return
    */
  private[refuel] def acceptedClass[X](x: Class[X]): Boolean

  /**
    * When access from any instance is permitted, it returns true if the request source instance matches.
    *
    * @param x request source instance
    * @return
    */
  private[refuel] def acceptedInstance(x: Any): Boolean
}
