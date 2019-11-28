package refuel.container

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater

import refuel.Types
import refuel.Types.@@
import refuel.container.indexer.{CanBeClosedIndexer, Indexer}
import refuel.effect.{Effect, EffectLike}
import refuel.injector.scope.{CanBeRestrictedSymbol, IndexedSymbol, TypedAcceptContext}
import refuel.internal.AtomicUpdater
import refuel.provider.Tag

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

private[refuel] object DefaultContainer {
  def apply(buffer: ContainerPool = TrieMap.empty, lights: Vector[Container] = Vector.empty): DefaultContainer = {
    val r = new DefaultContainer(lights)
    r._buffer = buffer
    r
  }
}

private[refuel] class DefaultContainer private(val lights: Vector[Container] = Vector.empty)
  extends AtomicUpdater[DefaultContainer, ContainerPool]
    with Container
    with Tag[Types.Localized] {

  val updater: AtomicReferenceFieldUpdater[DefaultContainer, ContainerPool] = {
    AtomicReferenceFieldUpdater.newUpdater(
      classOf[DefaultContainer],
      classOf[ContainerPool],
      "_buffer")
  }

  override def snapshot(w: ContainerPool): ContainerPool = w.snapshot()

  @volatile private[refuel] var _buffer: ContainerPool = TrieMap.empty

  /**
   * Cache in the injection container.
   *
   * @param value injection object
   * @tparam T injection type
   * @return
   */
  private[refuel] final def cache[T](value: IndexedSymbol[T]): IndexedSymbol[T] = {
    val key = ContainerIndexedKey(value.tag)
    atomicUpdate { nbf =>
      nbf.get(key) match {
        case None    => nbf.+=(key -> mutable.LinkedHashSet.apply(value))
        case Some(x) =>
          nbf.update(key, x + value)
          nbf
      }
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
  def find[T, A: TypedAcceptContext](tpe: universe.Type, requestFrom: A): Option[T] = {
    get.get(ContainerIndexedKey(tpe)) match {
      case None    => None
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
  def find[T: WeakTypeTag, A: TypedAcceptContext](requestFrom: A): Option[T] = {
    find[T, A](implicitly[WeakTypeTag[T]].tpe, requestFrom)
  }

  /**
   * May return an injectable object.
   *
   * @return
   */
  private[refuel] def findEffect: Set[EffectLike] = {
    get.get(ContainerIndexedKey(implicitly[WeakTypeTag[Effect]].tpe)) match {
      case None    => Set.empty
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
  private[refuel] def createIndexer[T: WeakTypeTag](x: T, priority: Int, lights: Vector[Container]): Indexer[T] = {
    new CanBeClosedIndexer(createScope[T](x, priority), lights :+ this)
  }

  /**
   * Generate open scope.
   *
   * @param x        Injectable object.
   * @param priority priority
   * @tparam T injection type
   * @return
   */
  private[refuel] override def createScope[T: universe.WeakTypeTag](x: T, priority: Int): IndexedSymbol[T] = {
    CanBeRestrictedSymbol[T](x, priority, weakTypeTag[T].tpe, this)
  }

  private[refuel] override def shading: @@[Container, Types.Localized] = {
    DefaultContainer(
      buffer = get,
      lights = this.lights.:+(this)
    )
  }
}
