package refuel.container

import refuel.container.indexer.Indexer
import refuel.container.provider.TypedAcceptContext
import refuel.container.provider.restriction.SymbolRestriction
import refuel.inject.InjectionPriority
import refuel.inject.Types.{@@, Localized}

import scala.reflect.runtime.universe._

trait Container {

  private[refuel] val lights: Vector[Container]

  /**
    * May return an injectable object.
    *
    * @param requestFrom Inject caller.
    * @return
    */
  def find[T, A: TypedAcceptContext](key: scala.Symbol, requestFrom: A): Option[T]

  /**
    * Generate an indexer.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  def createIndexer[T](
      k: scala.Symbol,
      x: T,
      priority: InjectionPriority,
      lights: Vector[Container] = this.lights
  ): Indexer[T]

  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  private[refuel] def cache[T](value: SymbolRestriction[T]): SymbolRestriction[T]

  /**
    * Generate open scope.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  private[refuel] def createScope[T: WeakTypeTag](
      k: scala.Symbol,
      x: T,
      priority: InjectionPriority
  ): SymbolRestriction[T]

  private[refuel] def shading: Container @@ Localized

  private[refuel] def fully[T, A: TypedAcceptContext](key: scala.Symbol, requestFrom: A): Iterable[T]
}
