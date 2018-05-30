package com.giitan.container

import scala.reflect.runtime.universe._

trait Indexer {

  /**
    * Resist dependencies.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @tparam T
    */
  def indexing[T: TypeTag](tag: TypeTag[T], value: T): Unit
}
