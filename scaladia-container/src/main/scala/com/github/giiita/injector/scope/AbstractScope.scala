package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

abstract class AbstractScope[T](val tag: WeakTypeTag[T]) extends InjectableScope[T] {
  def isSameAs[X: WeakTypeTag]: Boolean = {
    tag.tpe =:= implicitly[WeakTypeTag[X]].tpe
  }
}
