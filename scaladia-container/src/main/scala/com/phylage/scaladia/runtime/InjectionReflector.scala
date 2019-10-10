package com.phylage.scaladia.runtime

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe

trait InjectionReflector {
  /**
    * Create injection applyment.
    *
    * @tparam T injection type
    * @return
    */
  def reflectClass[T: universe.WeakTypeTag](c: Container)(x: Set[universe.ClassSymbol]): Set[IndexedSymbol[T]]

  /**
    * Create injection applyment.
    *
    * @tparam T injection type
    * @return
    */
  def reflectModule[T: universe.WeakTypeTag](c: Container)(x: Set[universe.ModuleSymbol]): Set[IndexedSymbol[T]]

  /**
    * Reflect to a runtime class.
    *
    * @param t Type symbol.
    * @return
    */
  def reflectClass(t: universe.Type): universe.RuntimeClass
}
