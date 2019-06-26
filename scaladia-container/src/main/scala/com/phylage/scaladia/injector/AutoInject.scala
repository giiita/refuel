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
trait AutoInject[T] extends AutoInjectable with Injector {
  me: T =>

  private[scaladia] val injectionPriority = 1000

  import scala.reflect.runtime.universe._

  def flush[N <: T: WeakTypeTag]: InjectableScope[T] = {
    implicitly[Container].createIndexer[T](
      me,
      injectionPriority
    )(implicitly[WeakTypeTag[N]].asInstanceOf[WeakTypeTag[T]]).indexing()
  }
}
