package com.github.giiita.container.indexer

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.{AcceptedFromInstanceScope, AcceptedFromTypeScope, InjectableScope, OpenScope}

import scala.reflect.{ClassTag, classTag}

class BroadSenseIndexer[T](scope: OpenScope[T]) extends AbstractIndexer[T] {
  override def indexing()(implicit ctn: Container): InjectableScope[T] = ctn.cache(scope)

  def accept[X](x: X): Indexer[T] = new NarrowInstanceIndexer(
    AcceptedFromInstanceScope(
      scope.value,
      scope.priority,
      scope.x,
      Vector(x)
    )
  )

  def accept[X: ClassTag]: Indexer[T] = new NarrowTypeIndexer(
    AcceptedFromTypeScope(
      scope.value,
      scope.priority,
      scope.x,
      Vector(classTag[X].runtimeClass)
    )
  )
}
