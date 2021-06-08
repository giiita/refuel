package refuel.container.indexer

import refuel.container.provider.restriction.{InstanceSymbol, SymbolRestriction}
import refuel.container.Container

import scala.reflect.ClassTag

/**
  * Indexer into container.
  * In some cases, it may index into multiple containers.
  * An accessible object can be registered for the symbol of this Indexer.
  *
  * @param scope Symbol which may be indexed
  * @param cnt   containers
  * @tparam T Symbol type
  */
class NarrowInstanceIndexer[T](scope: InstanceSymbol[T], cnt: Vector[Container])
    extends Indexer[T] {

  /**
    * Index a new symbol in the injection container.
    *
    * @return
    */
  override def indexing(): SymbolRestriction[T] = {
    cnt.foreach(_.cache(scope))
    scope
  }

  /**
    * Create a new authorization class for this indexer.
    * If you do this, you will not be able to register the authorization instance.
    *
    * @tparam X Accept class
    * @return
    */
  def accept[X](claz: Class[X]): Indexer[T] = unsupport

  /**
    * Register a new authorization instance with this indexer.
    * If you do this, you will not be able to register the authorization class.
    *
    * @param x Accept instance
    * @tparam X Accept instance type
    * @return
    */
  def accept[X](x: X): Indexer[T] = new NarrowInstanceIndexer(
    scope.copy(acceptedFrom = scope.acceptedFrom.+:(x)),
    cnt
  )
}
