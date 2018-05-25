package com.giitan.container

import scala.reflect.runtime.universe._
import com.giitan.box.Container
import com.giitan.injector.Injector

object FunctIndexer extends Indexer {

  /**
    * Resist dependencies.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    */
  def indexing[T: TypeTag, S <: Injector: TypeTag](tag: TypeTag[T], value: T, scope: S): Unit = {
    implicitly[Container].indexing(tag, value, scope.getClass)
  }
}
