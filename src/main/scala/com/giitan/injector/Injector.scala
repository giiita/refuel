package com.giitan.injector

import com.giitan.box.Container
import com.giitan.container._
import com.giitan.scope.Scope

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait Injector {me =>
  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @return
    */
  def inject[T: TypeTag: ClassTag]: T = {
    mount()
    implicitly[Container].find(typeTag[T], me)
  }

  /**
    * Regist a dependency object.
    * By default, it is only accessible from classes that inherit Injector that injected dependencies.
    * In case of extending it, accept () is performed individually.
    *
    * @param v Injectly object.
    * @tparam X Injectly type
    * @return
    */
  def narrow[X: TypeTag](v: X): Scope[X] = {
    FunctIndexer.indexing(typeTag[X], v, me)
    Scope[X]
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
    FunctIndexer.indexing(typeTag[X], v, me)
    Scope[X].acceptedGlobal
  }

  /**
    * When injecting a dependency with a member of object, it is injected at object initialization
    * by the static initializer, so there are cases where you can not overwrite dependency registration.
    *
    * In that case, you can register the dependency relationship declared with mount before injecting.
    */
  def mount(): Unit = {}
}
