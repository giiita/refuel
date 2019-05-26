package com.giitan.injectable

private[giitan] trait StoredDependency[X] {
  /**
    * Dependency search function.
    */
  protected val dependencyGet: () => X
  /**
    * Recovery hook when failure of dependency search fails.
    */
  protected val recoverHook: PartialFunction[Throwable, X]
  /**
    * Delayed evaluated Object container.
    */
  protected var lazyProvided: Option[X] = None

  /**
    * Recovery hook when failure of dependency search fails.
    *
    * @param function Recovery hook.
    * @return
    */
  def recover(function: PartialFunction[Throwable, X]): StoredDependency[X]

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def provide: X
}