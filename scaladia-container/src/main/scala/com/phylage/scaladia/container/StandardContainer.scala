package com.phylage.scaladia.container

import com.phylage.scaladia.Types
import com.phylage.scaladia.Types.@@
import com.phylage.scaladia.container.indexer.{BroadSenseIndexer, Indexer}
import com.phylage.scaladia.effect.{Effect, EffectLike}
import com.phylage.scaladia.injector.scope.{IndexedSymbol, OpenScope, TypedAcceptContext}
import com.phylage.scaladia.provider.Tag

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

private[scaladia] class StandardContainer(buffer: ContainerPool = TrieMap.empty, val lights: Vector[Container] = Vector.empty) extends Container with Tag[Types.Localized] {

  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  private[scaladia] def cache[T](value: IndexedSymbol[T]): IndexedSymbol[T] = synchronized {

    val key = ContainerIndexedKey(value.tag)
    buffer.get(key) match {
      case None => buffer.+=(key -> mutable.LinkedHashSet.apply(value))
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
  private[scaladia] def find[T, A: TypedAcceptContext](tpe: universe.Type, requestFrom: A): Option[T] = {
    buffer.get(ContainerIndexedKey(tpe.toString)) match {
      case None => None
      case Some(r) =>
        r.filter(x => x.c == this && x.accepted(requestFrom))
          .toSeq
          .sortBy(_.priority)(Ordering.Int.reverse)
          .headOption
          .map(_.value.asInstanceOf[T])
    }
  }

  /**
    * May return an injectable object.
    *
    * @param requestFrom object that called inject
    * @tparam T return object type
    * @return
    */
  private[scaladia] def find[T: WeakTypeTag, A: TypedAcceptContext](requestFrom: A): Option[T] = {
    find[T, A](implicitly[WeakTypeTag[T]].tpe, requestFrom)
  }

  /**
    * May return an injectable object.
    *
    * @return
    */
  private[scaladia] def findEffect: Set[EffectLike] = {
    buffer.get(ContainerIndexedKey(implicitly[WeakTypeTag[Effect]].tpe)) match {
      case None => Set.empty
      case Some(x) =>
        x.map(_.asInstanceOf[IndexedSymbol[Effect]])
          .filter(_.value.activate)
          .map(_.value)
          .toSet
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
  private[scaladia] def createIndexer[T: WeakTypeTag](x: T, priority: Int, lights: Vector[Container]): Indexer[T] = {
    new BroadSenseIndexer(createScope[T](x, priority), lights :+ this)
  }

  /**
    * Generate open scope.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  private[scaladia] override def createScope[T: universe.WeakTypeTag](x: T, priority: Int): IndexedSymbol[T] = OpenScope[T](x, priority, weakTypeTag[T].tpe, this)

  private[scaladia] override def shading: @@[Container, Types.Localized] = {
    new StandardContainer(
      buffer = buffer.snapshot(),
      lights = this.lights.:+(this)
    )
  }
}
