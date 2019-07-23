package com.phylage.scaladia.injector

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.InjectableScope

/**
  *
  *
  * It is a basic automatic injection interface.
  * The priority is fixed, use [[com.phylage.scaladia.injector.AutoInjectCustomPriority]] to change it.
  *
  * @tparam T Type to register
  */
trait AutoInject[T] extends AutoInjectable[T] with Injector {
  me: T =>

  private[scaladia] val injectionPriority = 1000

  import scala.reflect.runtime.universe._

  def flush(c: Container)(implicit wtt: WeakTypeTag[T]): InjectableScope[T] = {
    c.createIndexer[T](
      me,
      injectionPriority
    )(implicitly[WeakTypeTag[T]]).indexing()
  }
}
