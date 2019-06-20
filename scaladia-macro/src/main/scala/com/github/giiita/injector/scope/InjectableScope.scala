package com.github.giiita.injector.scope

import com.github.giiita.provider.Accessor

import scala.reflect.runtime.universe._

trait InjectableScope[T] {
  val value: T
  val priority: Int
  val tag: WeakTypeTag[T]

  final def accepted[X: WeakTypeTag](reqestFrom: Accessor[_]): Boolean = {

    isSameAs[X] && {
      isOpen || acceptedClass(reqestFrom.t.getClass) || acceptedInstance(reqestFrom.t)
    } match {
      case result =>
        println(s"$value = $result ($priority)")
        result
    }
  }

  protected def isOpen: Boolean = false

  protected def acceptedClass[X](x: Class[X]): Boolean

  protected def acceptedInstance[X](x: Any): Boolean

  protected def isSameAs[X: WeakTypeTag]: Boolean
}
