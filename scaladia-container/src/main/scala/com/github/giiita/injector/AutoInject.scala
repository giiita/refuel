package com.github.giiita.injector

import com.github.giiita.container.Container
import com.github.giiita.injector.scope.InjectableScope

trait AutoInject[T] extends AutoInjectable with Injector {
  me: T =>

  private[giiita] val injectionPriority = 1000

  import scala.reflect.runtime.universe._

  private[giiita] def flush[N <: T: WeakTypeTag]: InjectableScope[T] = {
    implicitly[Container].createIndexer[T](
      me,
      injectionPriority
    )(implicitly[WeakTypeTag[N]].asInstanceOf[WeakTypeTag[T]]).indexing()
  }
}
