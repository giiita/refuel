package com.github.giiita

import com.github.giiita.container.indexer.{BroadSenseIndexer, Indexer}
import com.github.giiita.injector.scope.{InjectableScope, OpenScope}

import scala.collection.mutable.ListBuffer

import scala.reflect.runtime.universe._

package object container {
  object DefaultContainer extends StorePublisherContainer {

    val buffer: ListBuffer[InjectableScope[_]] = ListBuffer()

    def cache[T](value: InjectableScope[T]): InjectableScope[T] = {
      value match {
        case sc if buffer.contains(sc) => sc
        case sc                        =>
          buffer += sc
          sc
      }
    }

    def getBuffer: ListBuffer[InjectableScope[_]] = buffer

    def createIndexer[T: WeakTypeTag](x: T, priority: Int): Indexer[T] = {
      new BroadSenseIndexer(OpenScope[T](x, priority, weakTypeTag[T]))
    }
  }
}
