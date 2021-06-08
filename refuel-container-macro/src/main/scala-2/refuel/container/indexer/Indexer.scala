package refuel.container.indexer

import refuel.container.provider.restriction.SymbolRestriction

import scala.reflect.ClassTag

/**
  * This is an indexer to register new objects in the injection container.
  *
  * @tparam T
  */
trait Indexer[T] {
  me =>

  /**
    * Index a new symbol in the injection container.
    *
    * @return
    */
  def indexing(): SymbolRestriction[T]

  /**
    * Create a new authorization class for this indexer.
    * If you do this, you will not be able to register the authorization instance.
    *
    * @tparam X Accept class
    * @return
    */
  def accept[X: ClassTag]: Indexer[T]

  /**
    * Register a new authorization instance with this indexer.
    * If you do this, you will not be able to register the authorization class.
    *
    * @param x Accept instance
    * @tparam X Accept instance type
    * @return
    */
  def accept[X](x: X): Indexer[T]

  final def unsupport: Indexer[T] = {
    throw new refuel.inject.exception.UnExceptedOperateException(
      "If you have already authorized somethings, you can not authorize other type."
    )
  }
}
