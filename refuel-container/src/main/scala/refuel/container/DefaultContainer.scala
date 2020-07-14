package refuel.container

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater

import refuel.Types
import refuel.Types.@@
import refuel.container.indexer.{CanBeClosedIndexer, Indexer}
import refuel.domination.InjectionPriority
import refuel.effect.EffectLike
import refuel.injector.scope.{CanBeRestrictedSymbol, IndexedSymbol, TypedAcceptContext}
import refuel.internal.AtomicUpdater
import refuel.internal.di.Effect
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

private[refuel] class DefaultContainer private (val lights: Vector[Container] = Vector.empty)
    extends AtomicUpdater[DefaultContainer, ContainerPool]
    with Container
    with Tag[Types.Localized] {

  val updater: AtomicReferenceFieldUpdater[DefaultContainer, ContainerPool] = {
    AtomicReferenceFieldUpdater.newUpdater(classOf[DefaultContainer], classOf[ContainerPool], "_buffer")
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
    _buffer.readOnlySnapshot().get(key) match {
      case None    => _buffer.+=(key -> new mutable.HashSet().+=(value))
      case Some(x) => _buffer.update(key, x.+=(value))
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
    _buffer.snapshot().get(ContainerIndexedKey(tpe)) match {
      case None => None
      case Some(r) =>
        val xx = r
          .filter(x => x.c == this && x.accepted(requestFrom))
          .toSeq
          .sortBy(_.priority)(InjectionPriority.Order)
        r.filter(x => x.c == this && x.accepted(requestFrom))
          .toSeq
          .sortBy(_.priority)(InjectionPriority.Order)
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
    _buffer.readOnlySnapshot().get(ContainerIndexedKey(implicitly[WeakTypeTag[Effect]].tpe)) match {
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
  def createIndexer[T: WeakTypeTag](x: T, priority: InjectionPriority, lights: Vector[Container]): Indexer[T] = {
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
  private[refuel] override def createScope[T: universe.WeakTypeTag](
      x: T,
      priority: InjectionPriority
  ): IndexedSymbol[T] = {
    CanBeRestrictedSymbol[T](x, priority, weakTypeTag[T].tpe, this)
  }

  private[refuel] override def shading: @@[Container, Types.Localized] = {
    DefaultContainer(
      buffer = _buffer.snapshot(),
      lights = this.lights.:+(this)
    )
  }
}
