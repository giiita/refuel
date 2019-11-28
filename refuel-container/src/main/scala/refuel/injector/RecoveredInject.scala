package refuel.injector

/**
  * This is registered as the weakest dependency that is applied when there is no injectable dependency.
  * @tparam T Type to register
  */
trait RecoveredInject[T] extends AutoInject[T] with Injector { me: T =>
  override val injectionPriority = 0
}
