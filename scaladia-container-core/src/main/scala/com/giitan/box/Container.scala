package com.giitan.box

import com.giitan.container.{ContainerMaster, Reflector}
import com.giitan.injectable.{Injectable, StoredDependency}
import com.giitan.injector.Injector
import com.giitan.loader.RichClassCrowds.ClassCrowds

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object Container {

  implicit private[giitan] object Master extends ContainerMaster {

    /**
      * Create StoredDependency.
      *
      * @param tag   Dependency object typed tag.
      * @param scope Typed objects to be accessed.
      * @tparam T
      * @tparam S
      * @return
      */
    private[giitan] def find[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S): StoredDependency[T] = Reflector(tag, scope)
  }

}

private[giitan] trait Container[ST[_]] {
  /**
    * Injectable object mapper.
    */
  val container: ListBuffer[Injectable[_, ST]]
  private[giitan] val automaticDependencies: ClassCrowds

  /**
    * Search accessible dependencies.
    *
    * @param tag   Dependency object typed tag.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    * @return
    */
  private[giitan] def search[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: ST[S]): Option[T]

  /**
    * Regist dependencies.
    *
    * @param whatType        Dependency object typed tag.
    * @param whatObject      Dependency object.
    * @param whereAccessFrom Acceptable scopes.
    * @tparam T
    */
  private[giitan] def indexing[T](whatType: TypeTag[T], whatObject: T, whereAccessFrom: Seq[ST[_]]): Unit
}
