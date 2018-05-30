package com.giitan.container

import scala.reflect.runtime.universe._
import com.giitan.box.Container

object FunctIndexer extends Indexer {

  /**
    * Resist dependencies.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @tparam T
    */
  def indexing[T: TypeTag](tag: TypeTag[T], value: T): Unit = {
    implicitly[Container].indexing(tag, value)
  }
}
