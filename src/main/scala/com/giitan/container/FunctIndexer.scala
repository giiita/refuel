package com.giitan.container

import scala.reflect.runtime.universe._
import com.giitan.box.Container
import com.giitan.scope.Scope.ScopeType

object FunctIndexer extends Indexer {

  /**
    * Resist dependencies.
    *
    * @param tag   Dependency object typed tag.
    * @param value Dependency object.
    * @param scope Acceptable scope.
    * @tparam T
    */
  def indexing[T](tag: TypeTag[T], value: T, scope: Seq[ScopeType]): Unit = {
    implicitly[Container].indexing(tag, value, scope)
  }
}
