package com.github.giiita.injector

import com.github.giiita.container.DefaultContainer
import com.github.giiita.injector.scope.InjectableScope

trait AutoInject[T] extends AutoInjectable with Injector {
  me: T =>

  private[giiita] val injectionPriority = 1000

  import scala.reflect.runtime.universe._

  private[giiita] def flush[N <: T](implicit x: WeakTypeTag[N]): InjectableScope[T] = {
    DefaultContainer.cache[T](me, injectionPriority)(x.asInstanceOf[WeakTypeTag[T]])
  }
}
