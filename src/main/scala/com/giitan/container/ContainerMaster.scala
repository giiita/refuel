package com.giitan.container

import com.giitan.injectable.StoredDependency
import com.giitan.injector.Injector

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait ContainerMaster {
  /**
    * Create StoredDependency.
    *
    * @param tag   Dependency object typed tag.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    * @return
    */
  private[giitan] def find[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S): StoredDependency[T]
}