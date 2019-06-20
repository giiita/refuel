package com.github.giiita.container.indexer

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.{AcceptedFromInstanceScope, InjectableScope}

import scala.reflect.ClassTag

class NarrowInstanceIndexer[T](scope: AcceptedFromInstanceScope[T]) extends AbstractIndexer[T] {
  override def indexing()(implicit ctn: Container): InjectableScope[T] = ctn.cache(scope)

  def accept[X: ClassTag]: Indexer[T] = scratch

  def accept[X](x: X): Indexer[T] = new NarrowInstanceIndexer(
    scope.copy(acceptedFrom = scope.acceptedFrom.+:(x))
  )
}
