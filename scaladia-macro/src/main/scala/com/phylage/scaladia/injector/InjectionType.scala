package com.phylage.scaladia.injector

import com.phylage.scaladia.injector.scope.InjectableScope

import scala.reflect.runtime.universe._

object InjectionType {
  def apply[T](applyment: () => InjectableScope[T])(implicit wtt: WeakTypeTag[T]): InjectionType[T] = InjectionType(wtt, applyment)
}
case class InjectionType[T](wtt: WeakTypeTag[T], applyment: () => InjectableScope[T])
