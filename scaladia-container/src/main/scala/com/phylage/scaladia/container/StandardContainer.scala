package com.phylage.scaladia.container

import com.phylage.scaladia.Types
import com.phylage.scaladia.Types.@@
import com.phylage.scaladia.container.indexer.{BroadSenseIndexer, Indexer}
import com.phylage.scaladia.effect.{Effect, EffectLike}
import com.phylage.scaladia.injector.scope.{InjectableScope, OpenScope}
import com.phylage.scaladia.provider.{Accessor, Tag}

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.reflect.runtime.universe._

class StandardContainer(buffer: ContainerPool = TrieMap.empty, val lights: Vector[Container] = Vector.empty) extends Container with Tag[Types.Localized] {

  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  def cache[T](value: InjectableScope[T]): InjectableScope[T] = synchronized {

    val key = ContainerIndexedKey(value.tag)
    buffer.get(key) match {
      case None    => buffer.+=(key -> mutable.LinkedHashSet.apply(value))
      case Some(x) => buffer.update(key, x + value)
    }
    value
  }

  /**
    * May return an injectable object.
    *
    * @param requestFrom object that called inject
    * @tparam T return object type
    * @return
    */
  def find[T: WeakTypeTag](requestFrom: Accessor[_]): Option[T] = {
    buffer.get(ContainerIndexedKey(implicitly[WeakTypeTag[T]])) match {
      case None    => None
      case Some(x) => x.filter(_.accepted(requestFrom)).toSeq.sortBy(_.priority)(Ordering.Int.reverse).headOption.map(_.value.asInstanceOf[T])
    }
  }

  /**
    * May return an injectable object.
    *
    * @return
    */
  def findEffect: Option[EffectLike] = {
    buffer.get(ContainerIndexedKey(implicitly[WeakTypeTag[Effect]])) match {
      case None    => None
      case Some(x) =>
        x.map(_.value.asInstanceOf[InjectableScope[Effect]])
          .filter(_.value.activate)
          .toSeq
          .sortBy(_.priority)(Ordering.Int.reverse)
          .headOption
          .map(_.value)
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
    new StandardContainer(
      buffer = buffer.snapshot(),
      lights = this.lights.:+(this)
    )
  }
}
