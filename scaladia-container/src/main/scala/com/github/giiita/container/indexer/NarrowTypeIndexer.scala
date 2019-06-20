package com.github.giiita.container.indexer

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.{AcceptedFromTypeScope, InjectableScope}

import scala.reflect.{ClassTag, classTag}

class NarrowTypeIndexer[T](scope: AcceptedFromTypeScope[T]) extends AbstractIndexer[T] {
  override def indexing()(implicit ctn: Container): InjectableScope[T] = ctn.cache(scope)

  def accept[X: ClassTag]: Indexer[T] = new NarrowTypeIndexer(
    scope.copy(acceptedFrom = scope.acceptedFrom.+:(classTag[X].runtimeClass))
  )

  def accept[X](x: X): Indexer[T] = scratch
}
