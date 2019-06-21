package com.github.giiita.container.indexer

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.{AcceptedFromInstanceScope, AcceptedFromTypeScope, InjectableScope, OpenScope}

import scala.reflect.{ClassTag, classTag}

class BroadSenseIndexer[T](scope: OpenScope[T]) extends AbstractIndexer[T] {
  /**
    * Create a new object in the injection container.
    *
    * @param ctn Container
    * @return
    */
  override def indexing()(implicit ctn: Container): InjectableScope[T] = ctn.cache(scope)

  /**
    * Register a new authorization instance with this indexer.
    * If you do this, you will not be able to register the authorization class.
    *
    * @param x Accept instance
    * @tparam X Accept instance type
    * @return
    */
  def accept[X](x: X): Indexer[T] = new NarrowInstanceIndexer(
    AcceptedFromInstanceScope(
      scope.value,
      scope.priority,
      scope.x,
      Vector(x)
    )
  )

  /**
    * Create a new authorization class for this indexer.
    * If you do this, you will not be able to register the authorization instance.
    *
    * @tparam X Accept class
    * @return
    */
  def accept[X: ClassTag]: Indexer[T] = new NarrowTypeIndexer(
    AcceptedFromTypeScope(
      scope.value,
      scope.priority,
      scope.x,
      Vector(classTag[X].runtimeClass)
    )
  )
}
