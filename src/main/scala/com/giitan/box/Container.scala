package com.giitan.box

import com.giitan.injectable.{Injectable, StoredDependency}
import com.giitan.injector.Injector
import com.giitan.loader.RichClassCrowds.ClassCrowds

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] trait Container[ST[_]] {
  /**
    * Injectable object mapper.
    */
  val container: ListBuffer[Injectable[_, ST]]

  private[giitan] val automaticDependencies: ClassCrowds

  /**
    * Search accessible dependencies.
    *
    * @param whatType  Dependency object typed tag.
    * @param whereFrom Typed objects to be accessed.
    * @tparam T
    * @tparam S
    * @return
    */
  private[giitan] def find[T: TypeTag : ClassTag, S <: Injector : TypeTag](whatType: TypeTag[T], whereFrom: ST[S]): StoredDependency[T]

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
