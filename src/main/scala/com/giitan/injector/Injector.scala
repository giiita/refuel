package com.giitan.injector

import com.giitan.box.Container._
import com.giitan.container._
import com.giitan.injectable.StoredDependency
import com.giitan.scope.Scope.ClassScope
import com.giitan.scope.{Scope, ScopeSet}

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait Injector {
  me =>

  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @param clazz Injectable class.
    * @return
    */
  def inject[T: TypeTag : ClassTag](clazz: Class[T]): StoredDependency[T] = {
    implicitly[ContainerMaster].find(typeTag[T], me)
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
  def narrow[X: TypeTag](v: X): ScopeSet[X] = Scope[X](v)

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
    inject[Indexer[ClassScope]].indexing(typeTag[X], v)
  }

  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @return
    */
  def inject[T: TypeTag : ClassTag]: StoredDependency[T] = {
    implicitly[ContainerMaster].find(typeTag[T], me)
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

}
