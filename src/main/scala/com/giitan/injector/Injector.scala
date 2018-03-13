package com.giitan.injector

import com.giitan.box.Container
import com.giitan.container._
import com.giitan.implicits.InjectorExpander
import com.giitan.scope.Closed

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

trait Injector extends InjectorExpander {me =>
  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @return
    */
  def inject[T: TypeTag: ClassTag]: T = {
    mount
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
  def depends[X: TypeTag](v: X): Closed[X] = {
    FunctIndexer.indexing(typeTag[X], v, me)
    Closed[X]
  }

  def mount: Unit = {}
}
