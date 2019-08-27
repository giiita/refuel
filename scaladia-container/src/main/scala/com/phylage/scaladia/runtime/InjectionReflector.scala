package com.phylage.scaladia.runtime

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe

trait InjectionReflector {
  def reflectClass[T: universe.WeakTypeTag](c: Container)(x: Set[universe.ClassSymbol]): Set[IndexedSymbol[T]]

  def reflectModule[T: universe.WeakTypeTag](c: Container)(x: Set[universe.ModuleSymbol]): Set[IndexedSymbol[T]]

  def reflectClass(t: universe.Type): universe.RuntimeClass
}
