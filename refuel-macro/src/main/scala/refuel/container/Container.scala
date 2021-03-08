package refuel.container

import refuel.Types.{@@, Localized}
import refuel.container.indexer.Indexer
import refuel.domination.InjectionPriority
import refuel.effect.EffectLike
import refuel.injector.scope.{IndexedSymbol, TypedAcceptContext}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

trait Container {

  private[refuel] val lights: Vector[Container]

  /**
    * Cache in the injection container.
    *
    * @param value injection object
    * @tparam T injection type
    * @return
    */
  private[refuel] def cache[T](value: IndexedSymbol[T]): IndexedSymbol[T]

  /**
    * May return an injectable object.
    *
    * @param requestFrom Inject caller.
    * @return
    */
  def find[T, A: TypedAcceptContext](tpe: universe.Type, requestFrom: A): Option[T]

  /**
    * May return an injectable object.
    *
    * @param requestFrom Inject caller.
    * @return
    */
  def find[T: WeakTypeTag, A: TypedAcceptContext](requestFrom: A): Option[T]

  /**
    * May return an injectable object.
    *
    * @return
    */
  private[refuel] def findEffect: Set[EffectLike]

  /**
    * Generate an indexer.
    *
    * @param x        Injectable object.
    * @param priority priority
    * @tparam T injection type
    * @return
    */
  def createIndexer[T: WeakTypeTag](
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
  private[refuel] def createScope[T: WeakTypeTag](x: T, priority: InjectionPriority): IndexedSymbol[T]

  private[refuel] def shading: Container @@ Localized

  private[refuel] def fully[T: WeakTypeTag]: Iterable[T]
}
