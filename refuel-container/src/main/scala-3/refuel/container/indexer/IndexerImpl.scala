package refuel.container.indexer

import refuel.container.Container
import refuel.container.provider.restriction.{InstanceSymbol, SymbolRestriction, TypedSymbol}

import scala.quoted.Type

/**
  * Indexer into container.
  * In some cases, it may index into multiple containers.
  * An accessible class or object can be registered for the symbol of this Indexer.
  *
  * @param scope Symbol which may be indexed
  * @param cnt   containers
  * @tparam T Symbol type
  */
class IndexerImpl[T](scope: SymbolRestriction[T], cnt: Vector[Container]) extends Indexer[T] {

  /**
    * Create a new object in the injection container.
    *
    * @return
    */
  override def indexing(): SymbolRestriction[T] = {
    cnt.foreach(_.cache(scope))
    scope
  }

  /**
    * Register a new authorization instance with this indexer.
    * If you do this, you will not be able to register the authorization class.
    *
    * @param x Accept instance
    * @tparam X Accept instance type
    * @return
    */
  def accept[X](x: X): Indexer[T] = new NarrowInstanceIndexer[T](
    InstanceSymbol(
      scope.key,
      scope.value,
      scope.priority,
      Vector(x)
    ),
    cnt
  )

  /**
    * Create a new authorization class for this indexer.
    * If you do this, you will not be able to register the authorization instance.
    *
    * @tparam X Accept class
    * @return
    */
  def accept[X](claz: Class[X]): Indexer[T] = new NarrowTypeIndexer[T](
    TypedSymbol(
      scope.key,
      scope.value,
      scope.priority,
      Vector(claz)
    ),
    cnt
  )
}
