package com.phylage.scaladia

import com.phylage.scaladia.container.indexer.{BroadSenseIndexer, Indexer}
import com.phylage.scaladia.injector.scope.{InjectableScope, OpenScope}
import com.phylage.scaladia.provider.Accessor

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

package object container {

  case class StandardContainer(buffer: ListBuffer[InjectableScope[_]] = ListBuffer()) extends Container {

    /**
      * Cache in the injection container.
      *
      * @param value injection object
      * @tparam T injection type
      * @return
      */
    def cache[T](value: InjectableScope[T]): InjectableScope[T] = {
      value match {
        case sc if buffer.contains(sc) => sc
        case sc                        =>
          buffer += sc
          sc
      }
    }

    /**
      * May return an injectable object.
      *
      * @return
      */
    def find[T: WeakTypeTag](requestFrom: Accessor[_]): Option[T] = {
      buffer.filter(_.accepted[T](requestFrom)).sortBy(_.priority).lastOption.map(_.value.asInstanceOf[T])

    }

    /**
      * Generate an indexer.
      *
      * @param x        Injectable object.
      * @param priority priority
      * @tparam T injection type
      * @return
      */
    def createIndexer[T: WeakTypeTag](x: T, priority: Int): Indexer[T] = {
      new BroadSenseIndexer(OpenScope[T](x, priority, weakTypeTag[T]), this)
    }

    override def shading: Container = copy()
  }

}
