package com.github.giiita.injector

import com.github.giiita.container.DefaultContainer

trait AutoInject[T] extends AutoInjectable with Injector {
  me: T =>

  private[injector] val injectionPriority = 1000

  import scala.reflect.runtime.universe._
  def flush[N <: T](implicit x: WeakTypeTag[N]): Unit = {
    DefaultContainer.flush[T](me, injectionPriority)(x.asInstanceOf[WeakTypeTag[T]])
  }
}
