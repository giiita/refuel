package refuel.runtime

import refuel.container.Container
import refuel.domination.InjectionPriority
import refuel.injector.InjectionPool
import refuel.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe

trait InjectionReflector {

  def embody[T](ms: universe.ModuleSymbol): T

  /**
    * Create injection applyment.
    *
    * @tparam T injection type
    * @return
    */
  def reflectClass[T: universe.WeakTypeTag](clazz: Class[_], ip: InjectionPool)(c: Container)(x: universe.ClassSymbol): InjectionPriority => IndexedSymbol[T]

  /**
    * Create injection applyment.
    *
    * @tparam T injection type
    * @return
    */
  def reflectModule[T: universe.WeakTypeTag](c: Container)(x: universe.ModuleSymbol): InjectionPriority => IndexedSymbol[T]

  /**
    * Reflect to a runtime class.
    *
    * @param t Type symbol.
    * @return
    */
  def reflectClass(t: universe.Type): universe.RuntimeClass
}
