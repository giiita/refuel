package com.phylage.scaladia.injector

import com.phylage.scaladia.injector.scope.InjectableScope

import scala.reflect.runtime.universe._

object InjectionType {
  def apply[T](applyment: () => InjectableScope[T], name: String)(implicit wtt: WeakTypeTag[T]): InjectionType[T] = InjectionType(wtt, applyment, name)
}
case class InjectionType[T](wtt: WeakTypeTag[T], applyment: () => InjectableScope[T], name: String) {
  def =:=[C](compare: InjectionType[C]): Boolean = name == compare.name
}
