package com.giitan.box

import com.giitan.injectable.Injectable
import com.giitan.injector.Injector

import scala.reflect.runtime.universe._

private[giitan] trait Container {
  /**
    * Injectable object mapper.
    */
  protected var v: Set[Injectable[_]]

  private[giitan] val automaticDependencies: Iterator[ModuleMirror]

  /**
    * Search accessible dependencies.
    *
    * @param tag Dependency object typed tag.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    * @return
    */
  private[giitan] def find[T, S <: Injector: TypeTag](tag: TypeTag[T], scope: S): T

  /**
    * Regist dependencies.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @param scope Typed objects to be accessed.
    * @tparam T
    * @tparam S
    */
  private[giitan] def indexing[T: TypeTag, S <: Injector: TypeTag](tag: TypeTag[T], value: T, scope: S): Unit

  /**
    * Condense the accessible type.
    *
    * @param typTag Dependency object type.
    */
  private[giitan] def scoped(typTag: Type): Unit

  /**
    * Extend the accessible type.
    *
    * @param typTag Dependency object type.
    */
  private[giitan] def scoped(clazz: Class[_], typTag: Type): Unit
}
