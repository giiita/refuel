package refuel.container.provider.restriction

import refuel.container.IndexedKey
import refuel.container.provider.TypedAcceptContext
import refuel.inject.InjectionPriority

import scala.quoted._

abstract class SymbolRestriction[T](
  val key: IndexedKey,
  val value: T,
  val priority: InjectionPriority) {
  /**
   * Determines if this object can be injected from any accessor.
   *
   * @param requestFrom Accessor of request source.
   * @return
   */
  final def accepted[X](requestFrom: X)(using x: TypedAcceptContext[X]): Boolean = {
    x.accepted(this)(requestFrom)
  }

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
  def isOpen: Boolean = true
}
