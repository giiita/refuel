package refuel.container.indexer

import refuel.container.Container
import refuel.container.provider.restriction.{SymbolRestriction, TypedSymbol}

import scala.reflect.{classTag, ClassTag}

/**
  * Indexer into container.
  * In some cases, it may index into multiple containers.
  * An accessible class can be registered for the symbol of this Indexer.
  *
  * @param scope Symbol which may be indexed
  * @param cnt   containers
  * @tparam T Symbol type
  */
class NarrowTypeIndexer[T](scope: TypedSymbol[T], cnt: Vector[Container]) extends Indexer[T] {

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
  def accept[X: ClassTag]: Indexer[T] = new NarrowTypeIndexer(
    scope.copy(
      acceptedFrom = scope.acceptedFrom.+:(classTag[X].runtimeClass)
    ),
    cnt
  )

  /**
    * Register a new authorization instance with this indexer.
    * If you do this, you will not be able to register the authorization class.
    *
    * @param x Accept instance
    * @tparam X Accept instance type
    * @return
    */
  def accept[X](x: X): Indexer[T] = unsupport
}
