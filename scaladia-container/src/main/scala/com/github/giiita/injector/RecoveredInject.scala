package com.github.giiita.injector

/**
  * This is registered as the weakest dependency that is applied when there is no injectible dependency.
  * @tparam T Type to register
  */
trait RecoveredInject[T] extends AutoInject[T] with Injector { me: T =>
  override val injectionPriority = 0
}
