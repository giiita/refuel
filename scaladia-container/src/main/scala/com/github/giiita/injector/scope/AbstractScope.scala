package com.github.giiita.injector.scope

import scala.reflect.runtime.universe._

abstract class AbstractScope[T](tag: WeakTypeTag[T]) extends InjectableScope[T] {
  def isSameAs[X: WeakTypeTag]: Boolean = {
    println(s"${tag.tpe} =:= ${implicitly[WeakTypeTag[X]].tpe} = ${tag.tpe =:= implicitly[WeakTypeTag[X]].tpe}")
    tag.tpe =:= implicitly[WeakTypeTag[X]].tpe
  }
}
