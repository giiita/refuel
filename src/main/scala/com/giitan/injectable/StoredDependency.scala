package com.giitan.injectable

trait StoredDependency[X] {
  protected val dependencyGet: () => X

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def provide: X = dependencyGet()
}