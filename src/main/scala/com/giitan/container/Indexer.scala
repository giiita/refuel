package com.giitan.container

import scala.reflect.runtime.universe._

trait Indexer[ST[_]] {

  /**
    * Resist dependencies.
    *
    * @param whatType        Dependency object typed tag.
    * @param whatObject      Dependency object.
    * @param whereAccessFrom Acceptable scopes
    * @tparam T
    */
  def indexing[T](whatType: TypeTag[T], whatObject: T, whereAccessFrom: Seq[ST[_]] = Nil): Unit
}
