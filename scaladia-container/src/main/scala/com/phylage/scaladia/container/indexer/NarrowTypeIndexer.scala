package com.phylage.scaladia.container.indexer

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.{AcceptedFromTypeScope, InjectableScope}

import scala.reflect.{ClassTag, classTag}

class NarrowTypeIndexer[T](scope: AcceptedFromTypeScope[T], cnt: Vector[Container]) extends AbstractIndexer[T] {
  /**
    * Create a new object in the injection container.
    *
    * @return
    */
  override def indexing(): InjectableScope[T] = {
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
  def accept[X](x: X): Indexer[T] = scratch
}
