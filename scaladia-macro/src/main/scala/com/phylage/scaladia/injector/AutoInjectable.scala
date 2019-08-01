package com.phylage.scaladia.injector

/**
  * This is subject to automatic loading by scaladia.
  */
trait AutoInjectable[T] {
  private[scaladia] val injectionPriority: Int
}