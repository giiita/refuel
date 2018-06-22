package com.giitan.injector

import com.giitan.box.Container
import com.giitan.container._
import com.giitan.injectable.StoredDependency
import com.giitan.scope.Scope

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait Injector {
  me =>
  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @return
    */
  def inject[T: TypeTag : ClassTag]: StoredDependency[T] = {
    implicitly[Container].find(typeTag[T], me)
  }

  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @param clazz Injectable class.
    * @return
    */
  def inject[T: TypeTag : ClassTag](clazz: Class[T]): StoredDependency[T] = {
    implicitly[Container].find(typeTag[T], me)
  }

  /**
    * Regist a dependency object.
    * By default, it is only accessible from classes that inherit Injector that injected dependencies.
    * In case of extending it, accept () is performed individually.
    * After defining the reference source in a narrow sense, "indexing()" does.
    *
    * @param v Injectly object.
    * @tparam X Injectly type
    * @return
    */
  def narrow[X: TypeTag](v: X): Scope[X] = {
    Scope[X](v).accept(me)
  }

  /**
    * Regist a dependency object.
    * By default, it is only accessible from all object.
    * The dependency relationship registered by this will not be overwritten.
    * However, objects registered narrow will be injected preferentially.
    *
    * @param v Injectly object.
    * @tparam X Injectly type
    * @return
    */
  def depends[X: TypeTag](v: X): Unit = {
    inject[Indexer].indexing(typeTag[X], v)
  }

  import scala.language.implicitConversions

  /**
    * Provide dependency.
    *
    * @param variable Stored dependency object.
    * @tparam X
    * @return
    */
  implicit def provide[X](variable: StoredDependency[X]): X = variable.provide

  implicit class AnyTIndexable[T](value: T) {
    def indexing[X >: T: TypeTag]: T = {
      depends[X](value)
      value
    }
  }
}
