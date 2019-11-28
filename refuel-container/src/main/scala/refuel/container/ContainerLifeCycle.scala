package refuel.container

/**
  * Storing container instance.
  * The container is not a singleton, but often needs to refer to the same container instance.
  * Therefore, manage the container life cycle.
  */
private[refuel] trait ContainerLifeCycle {
  val ctn: Container
  val ijp: refuel.injector.InjectionPool
}
