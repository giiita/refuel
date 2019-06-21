package com.phylage.scaladia.container.indexer

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.{AcceptedFromInstanceScope, InjectableScope}

import scala.reflect.ClassTag

class NarrowInstanceIndexer[T](scope: AcceptedFromInstanceScope[T]) extends AbstractIndexer[T] {
  /**
    * Create a new object in the injection container.
    *
    * @param ctn Container
    * @return
    */
  override def indexing()(implicit ctn: Container): InjectableScope[T] = ctn.cache(scope)

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
    scope.copy(acceptedFrom = scope.acceptedFrom.+:(x))
  )
}
