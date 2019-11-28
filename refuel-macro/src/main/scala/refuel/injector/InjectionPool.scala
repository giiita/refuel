package refuel.injector

import refuel.container.Container
import refuel.injector.InjectionPool.InjectionApplyment
import refuel.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe._

object InjectionPool {
  type InjectionApplyment[T] = Container => Set[IndexedSymbol[T]]
}

trait InjectionPool {

  /**
    * Get a list of injection-enabled declarations of any type
    *
    * @param wtt weak type tag.
    * @tparam T Type you are trying to get
    * @return
    */
  def collect[T](implicit wtt: WeakTypeTag[T]): InjectionApplyment[T]
}
