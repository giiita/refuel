package com.phylage.scaladia.container.indexer

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.{AcceptedFromInstanceScope, IndexedSymbol}

import scala.reflect.ClassTag

class NarrowInstanceIndexer[T](scope: AcceptedFromInstanceScope[T], cnt: Vector[Container]) extends AbstractIndexer[T] {
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
    * Create a new authorization class for this indexer.
    * If you do this, you will not be able to register the authorization instance.
    *
    * @tparam X Accept class
    * @return
    */
  def accept[X: ClassTag]: Indexer[T] = scratch

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
