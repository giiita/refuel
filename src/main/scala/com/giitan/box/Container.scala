package com.giitan.box

import com.giitan.box.ScaladiaClassLoader.RichClassCrowd
import com.giitan.injectable.{Injectable, StoredDependency}
import com.giitan.injector.Injector
import com.giitan.scope.Scope.ScopeType

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] trait Container {
  /**
    * Injectable object mapper.
    */
  val container: ListBuffer[Injectable[_]]

  private[giitan] val automaticDependencies: RichClassCrowd

  /**
    * Search accessible dependencies.
    *
    * @param tag   Dependency object typed tag.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    * @return
    */
  private[giitan] def find[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S): StoredDependency[T]

  /**
    * Regist dependencies.
    *
    * @param tag   Dependency object typed tag.
    * @param value Dependency object.
    * @tparam T
    */
  private[giitan] def indexing[T: TypeTag](tag: TypeTag[T], value: T): Unit

  /**
    * Condense the accessible type.
    *
    * @param typTag Dependency object type.
    */
  private[giitan] def globaly(typTag: Type): Unit

  /**
    * Extend the accessible type.
    *
    * @param targetType Dependency object type.
    */
  private[giitan] def appendScope(acceptableType: ScopeType, targetType: Type): Unit
}
