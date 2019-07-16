package com.phylage.scaladia.container.indexer

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.{AcceptedFromInstanceScope, AcceptedFromTypeScope, InjectableScope, OpenScope}

import scala.reflect.{ClassTag, classTag}

class BroadSenseIndexer[T](scope: OpenScope[T], cnt: Container) extends AbstractIndexer[T] {
  /**
    * Create a new object in the injection container.
    *
    * @return
    */
  override def indexing(): InjectableScope[T] = cnt.cache(scope)

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
    AcceptedFromTypeScope(
      scope.value,
      scope.priority,
      scope.x,
      Vector(classTag[X].runtimeClass)
    ),
    cnt
  )
}
