package com.github.giiita.container

import com.github.giiita.container.indexer.Indexer
import com.github.giiita.injector.scope.InjectableScope

import scala.collection.mutable.ListBuffer

import scala.reflect.runtime.universe._

trait Container {
  private[giiita]  def cache[T](value: InjectableScope[T]): InjectableScope[T]

  private[giiita] def getBuffer: ListBuffer[InjectableScope[_]]

  private[giiita] def createIndexer[T: WeakTypeTag](x: T, priority: Int): Indexer[T]
}
