package com.github.giiita.injector

trait RecoveredInject[T] extends AutoInject[T] with Injector { me: T =>
  override val injectionPriority = 0
}
