package com.phylage.scaladia

import com.phylage.scaladia.Types.@@
import com.phylage.scaladia.container.indexer.{BroadSenseIndexer, Indexer}
import com.phylage.scaladia.injector.scope.{InjectableScope, OpenScope}
import com.phylage.scaladia.provider.{Accessor, Tag}

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

package object container {

  case class StandardContainer(shade: Boolean = false, buffer: ListBuffer[InjectableScope[_]] = ListBuffer.empty, lights: Vector[Container] = Vector.empty) extends Container with Tag[Types.Localized] {

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
        case sc =>
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
      buffer.filter(_.accepted[T](requestFrom))
        .sortBy(_.priority) match {
        case x =>
          x.lastOption
            .map(_.value.asInstanceOf[T])
      }

    }

    /**
      * Generate an indexer.
      *
      * @param x        Injectable object.
      * @param priority priority
      * @tparam T injection type
      * @return
      */
    def createIndexer[T: WeakTypeTag](x: T, priority: Int, lights: Vector[Container]): Indexer[T] = {
      new BroadSenseIndexer(OpenScope[T](x, priority, weakTypeTag[T]), lights :+ this)
    }

    override def shading: @@[Container, Types.Localized] = {
      copy(
        shade = true,
        buffer = ListBuffer.apply(this.buffer.toSeq: _*),
        lights = this.lights.:+(this)
      )
    }
  }

}
