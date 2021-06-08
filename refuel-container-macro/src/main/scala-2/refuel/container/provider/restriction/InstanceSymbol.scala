package refuel.container.provider.restriction

import refuel.inject.InjectionPriority

private[refuel] case class InstanceSymbol[T](
    override val key: scala.Symbol,
    override val value: T,
    override val priority: InjectionPriority,
    acceptedFrom: Vector[Any]
) extends SymbolRestriction[T](key, value, priority) {

  /**
    * When permitting access from any class, it returns true if the class of the request source matches.
    *
    * @param x class of the request source
    * @tparam X class of the request source
    * @return
    */
  override def acceptedClass[X](x: Class[X]): Boolean = false

  /**
    * When access from any instance is permitted, it returns true if the request source instance matches.
    *
    * @param x request source instance
    * @return
    */
  override def acceptedInstance(x: Any): Boolean = {
    acceptedFrom.contains(x)
  }

  override def isOpen: Boolean = false
}
