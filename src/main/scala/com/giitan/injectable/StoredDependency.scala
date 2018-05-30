package com.giitan.injectable

trait StoredDependency[X] {
  private var lazyProvided: Option[X] = None
  protected val dependencyGet: () => X

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def provide: X = lazyProvided.synchronized {
    lazyProvided getOrElse {
      val module = dependencyGet()
      lazyProvided = Some(module)
      module
    }
  }
}