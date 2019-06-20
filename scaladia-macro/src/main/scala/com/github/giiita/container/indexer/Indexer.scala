package com.github.giiita.container.indexer

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.InjectableScope

import scala.reflect.ClassTag


trait Indexer[T] { me =>
  def indexing()(implicit ctn: Container): InjectableScope[T]

  def accept[X: ClassTag]: Indexer[T]
  def accept[X](x: X): Indexer[T]

  implicit def self: Indexer[T] = me
}
