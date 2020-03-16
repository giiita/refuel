package refuel.injector

import refuel.container.Container
import refuel.domination.InjectionPriority
import refuel.injector.InjectionPool.LazyConstruction
import refuel.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe._

object InjectionPool {
  type DependencyConstructor[T] = Container => Seq[LazyConstruction[T]]
  type LazyConstruction[T] = (InjectionPriority, Seq[InjectionPriority => IndexedSymbol[T]])
}

trait InjectionPool {

  /**
   * Get a list of injection-enabled declarations of any type.
   * Next to ModuleSymbol, reflect ClassSymbol.
   * A class / object with an effective annotation will be indexed into the container if it is an effective effect.
   *
   * @param wtt weak type tag.
   * @tparam T Type you are trying to get
   * @return
   */
  def collect[T](clazz: Class[_])(implicit wtt: WeakTypeTag[T]): Container => Option[LazyConstruction[T]]
}
