package com.phylage.scaladia.runtime

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.InjectableScope

import scala.reflect.runtime.universe

trait InjectionReflector {
  def reflect[T: universe.WeakTypeTag](c: Container)(x: Vector[universe.ModuleSymbol]): Vector[InjectableScope[T]]

  def reflectClass(t: universe.Type): universe.RuntimeClass
}
