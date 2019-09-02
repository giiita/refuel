package com.phylage.scaladia.injector

import com.phylage.scaladia.container.Container

import scala.reflect.runtime.universe

/**
  * Inject only once.
  * Basically the same behavior as AutoInject, but the container is not indexed.
  * If a class symbol that inherits InjectOnce is injected from a different location, an instance is created each time.
  *
  * @tparam T Type to register
  */
trait InjectOnce[T] extends AutoInject[T] { me: T =>
  override private[scaladia] def flush(c: Container)(implicit wtt: universe.WeakTypeTag[T]) = {
    c.createScope[T](
      me,
      injectionPriority
    )(wtt)
  }
}
