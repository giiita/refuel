package com.giitan.injector

/**
  * Injector to automatically inject into DI container.
  * Usage is same as [[Injector]]
  *
  * Injectable that inherits from this must be statically accessible,
  * such as top level declaration.
  */
@Deprecated
trait AutoInjector extends Injector