package com.giitan.container

import com.giitan.injector.Injector

import scala.reflect.runtime.universe._

trait Indexer {

  /**
    * Resist dependencies.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    */
  def indexing[T: TypeTag, S <: Injector: TypeTag](tag: TypeTag[T], value: T, scope: S): Unit
}
