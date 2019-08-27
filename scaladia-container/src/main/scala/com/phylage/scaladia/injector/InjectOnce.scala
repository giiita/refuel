package com.phylage.scaladia.injector

import com.phylage.scaladia.container.Container

import scala.reflect.runtime.universe

trait InjectOnce[T] extends AutoInject[T] { me: T =>
  override private[scaladia] def flush(c: Container)(implicit wtt: universe.WeakTypeTag[T]) = {
    c.createScope[T](
      me,
      injectionPriority
    )(wtt)
  }
}
