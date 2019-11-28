package refuel.injector

/**
  * This is subject to automatic loading by refuel.
  */
trait AutoInjectable[T] {
  private[refuel] val injectionPriority: Int
}