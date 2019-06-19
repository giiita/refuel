package com.github.giiita.injector

trait RecoveredInject[T] extends AutoInject[T] with Injector { me: T =>
  override private[injector] val injectionPriority = 0
}
