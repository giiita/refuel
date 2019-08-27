package com.phylage.scaladia.container.indexer

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.{AcceptedFromInstanceSymbol, AcceptedFromTypeSymbol, IndexedSymbol}

import scala.reflect.{ClassTag, classTag}

/**
  * Indexer into container.
  * In some cases, it may index into multiple containers.
  * An accessible class or object can be registered for the symbol of this Indexer.
  *
  * @param scope Symbol which may be indexed
  * @param cnt   containers
  * @tparam T Symbol type
  */
class CanBeClosedIndexer[T](scope: IndexedSymbol[T], cnt: Vector[Container]) extends AbstractIndexer[T] {
  /**
    * Create a new object in the injection container.
    *
    * @return
    */
  override def indexing(): IndexedSymbol[T] = {
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
  def accept[X](x: X): Indexer[T] = new NarrowInstanceIndexer(
    AcceptedFromInstanceSymbol(
      scope.value,
      scope.priority,
      scope.tag,
      Vector(x),
      scope.c
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
  def accept[X: ClassTag]: Indexer[T] = new NarrowTypeIndexer(
    AcceptedFromTypeSymbol(
      scope.value,
      scope.priority,
      scope.tag,
      Vector(classTag[X].runtimeClass),
      scope.c
    ),
    cnt
  )
}
