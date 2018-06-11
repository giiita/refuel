package com.giitan.container

import com.giitan.scope.Scope.ScopeType

import scala.reflect.runtime.universe._

trait Indexer {

  /**
    * Resist dependencies.
    *
    * @param tag   Dependency object typed tag.
    * @param value Dependency object.
    * @param scope Acceptable scopes
    * @tparam T
    */
  def indexing[T](tag: TypeTag[T], value: T, scope: Seq[ScopeType] = Nil): Unit
}
