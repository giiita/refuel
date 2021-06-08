package refuel.container

import refuel.container.provider.restriction.SymbolRestriction
import refuel.inject.InjectionPriority
import provider.TypedAcceptContext
import refuel.container.indexer.Indexer
import refuel.container.provider.Accessor
import refuel.inject.Types._

import scala.quoted._

trait Container {

  private[refuel] val lights: Vector[Container]

  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  private[refuel] def cache[T](value: SymbolRestriction[T]): SymbolRestriction[T]

  /**
    * May return an injectable object.
    *
    * @param requestFrom Inject caller.
    * @return
    */
  def find[T, A: TypedAcceptContext](key: Symbol, requestFrom: A): Option[T]

  /**
    * Generate an indexer.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  def createIndexer[T](
      k: IndexedKey,
      x: T,
      priority: InjectionPriority,
      lights: Vector[Container] = this.lights
  ): Indexer[T]

  /**
    * Generate open scope.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  private[refuel] def createScope[T](key: IndexedKey, x: T, priority: InjectionPriority): SymbolRestriction[T]

  private[refuel] def shading: Container @@ Localized

  private[refuel] def fully[T, A: TypedAcceptContext](key: IndexedKey, requestFrom: A): Iterable[T]
}
