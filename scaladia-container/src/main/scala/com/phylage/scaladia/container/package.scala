package com.phylage.scaladia

import com.phylage.scaladia.Types.@@
import com.phylage.scaladia.container.indexer.{BroadSenseIndexer, Indexer}
import com.phylage.scaladia.injector.scope.{InjectableScope, OpenScope}
import com.phylage.scaladia.provider.{Accessor, Tag}
import com.phylage.scaladia.runtime.InjectionReflector

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.reflect.runtime.universe._

package object container {
  type ContainerTypeKey = String
  type ContainerPool = TrieMap[ContainerTypeKey, mutable.LinkedHashSet[InjectableScope[_]]]

  implicit val injectionReflector: InjectionReflector = RuntimeReflector

  case class StandardContainer(shade: Boolean = false, buffer: ContainerPool = TrieMap.empty, lights: Vector[Container] = Vector.empty) extends Container with Tag[Types.Localized] {

    /**
      * Cache in the injection container.
      *
      * @param value injection object
      * @tparam T injection type
      * @return
      */
    def cache[T](value: InjectableScope[T]): InjectableScope[T] = synchronized {
      buffer.get(value.tag.tpe.toString) match {
        case None    => buffer.+=(value.tag.tpe.toString -> mutable.LinkedHashSet.apply(value))
        case Some(x) => buffer.update(value.tag.tpe.toString, x + value)
      }
      value
    }

    /**
      * May return an injectable object.
      *
      * @return
      */
    def find[T: WeakTypeTag](requestFrom: Accessor[_]): Option[T] = {
      buffer.get(implicitly[WeakTypeTag[T]].tpe.toString) match {
        case None    => None
        case Some(x) => x.filter(_.accepted(requestFrom)).toSeq.sortBy(_.priority)(Ordering.Int.reverse).headOption.map(_.value.asInstanceOf[T])
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
        buffer = buffer.snapshot(),
        lights = this.lights.:+(this)
      )
    }
  }

}
