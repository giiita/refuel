package com.phylage.scaladia.runtime

import com.phylage.scaladia.injector.InjectionPool.InjectionApplyment

import scala.reflect.runtime.universe

trait InjectionReflector {
  def reflect[T: universe.WeakTypeTag](x: universe.ModuleSymbol): InjectionApplyment[T]
}
