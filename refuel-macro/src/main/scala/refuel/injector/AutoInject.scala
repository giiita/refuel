package refuel.injector

/**
  * [[AutoInject]] is subject to automatic loading by refuel.
  *
  * At the time of calling `inject[T]`, all module classes that
  * inherit `T with AutoInjectable` are injection candidates.
  *
  * If there are multiple injection candidates, you need to be
  * controlled with the [[refuel.domination.Inject]] annotation.
  *
  * ```
  * trait A
  *
  * class B extends A with AutoInjectable // This is treated as priority "Default"
  *
  * @Inject[Finally]
  * class C extends A with AutoInjectable
  *
  * @Inject(Primary)
  * class D extends A with AutoInjectable // This will be injected.
  * ```
  *
  * This annotation is an attribute for automatic injection, so
  * it is not necessary when manually registering dependencies.
  */
trait AutoInject
