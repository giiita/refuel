package refuel.container

import refuel.container.indexer.{Indexer, IndexerImpl}
import refuel.container.internal.AtomicUpdater
import refuel.container.provider.Accessor
import refuel.container.provider.TypedAcceptContext
import refuel.container.provider.restriction.{OpenSymbol, SymbolRestriction}
import refuel.inject.Types.@@
import refuel.inject.{InjectionPriority, Tag, Types}

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.quoted._
import scala.quoted.staging._

private[refuel] object ContainerImpl {
  def apply(buffer: ContainerPool = TrieMap.empty, lights: Vector[Container] = Vector.empty): ContainerImpl = {
    val r = new ContainerImpl(lights)
    r._buffer = buffer
    r
  }
}

private[refuel] class ContainerImpl private (val lights: Vector[Container] = Vector.empty)
  extends AtomicUpdater[ContainerImpl, ContainerPool]
    with Container
    with Tag[refuel.inject.Types.Localized] {
  given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)

  val updater: AtomicReferenceFieldUpdater[ContainerImpl, ContainerPool] = {
    AtomicReferenceFieldUpdater.newUpdater(classOf[ContainerImpl], classOf[ContainerPool], "_buffer")
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
  private[refuel] final def cache[T](value: SymbolRestriction[T]): SymbolRestriction[T] = {
    _buffer.readOnlySnapshot().get(value.key) match {
      case None    => _buffer.+=(value.key -> new mutable.HashSet().+=(value))
      case Some(x) => _buffer.update(value.key, x.+=(value))
    }
    value
  }

  private[refuel] def _find[T, A: TypedAcceptContext](key: IndexedKey, requestFrom: A): Option[T] = {
    val x = _buffer.snapshot().get(key)
    _buffer.snapshot().get(key) match {
      case None => None
      case Some(r) =>
        r.filter(_.accepted(requestFrom)).toSeq.minByOption(_.priority.v).map(_.value.asInstanceOf[T])
    }
  }

  /**
   * May return an injectable object.
   *
   * @param requestFrom object that called inject
   * @tparam T return object type
   * @return
   */
  def find[T, A: TypedAcceptContext](key: IndexedKey, requestFrom: A): Option[T] = {
    _find[T, A](key, requestFrom).orElse(lights.lastOption.flatMap(_.find[T, A](key, requestFrom)))
  }

  /**
   * Generate an indexer.
   *
   * @param x        Injectable object.
   * @param priority priority
   * @tparam T injection type
   * @return
   */
  def createIndexer[T](k: IndexedKey, x: T, priority: InjectionPriority, lights: Vector[Container]): Indexer[T] = {
    new IndexerImpl[T](createScope[T](k, x, priority), lights :+ this)
  }

  /**
   * Generate open scope.
   *
   * @param x        Injectable object.
   * @param priority priority
   * @tparam T injection type
   * @return
   */
  private[refuel] override def createScope[T](
    key: IndexedKey,
    x: T,
    priority: InjectionPriority
  ): SymbolRestriction[T] = {
    OpenSymbol[T](key, x, priority)
  }

  private[refuel] override def shading: @@[Container, Types.Localized] = {
    new ContainerImpl(
      lights = this.lights.:+(this)
    )
  }

  private[refuel] def fully[T, A: TypedAcceptContext](key: IndexedKey, requestFrom: A): Iterable[T] = {
    _buffer.snapshot().get(key) match {
      case None => None
      case Some(r) =>
        r.filter(_.accepted(requestFrom)).toSeq.groupBy(_.priority.v).minBy(_._1)._2.map(_.value.asInstanceOf[T])
    }
  }
}
