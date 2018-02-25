package com.giitan.injector

import com.giitan.box.Container
import com.giitan.container._
import com.giitan.scope.Closed

import scala.reflect.runtime.universe._

trait Injector {me =>
  /**
    * Inject a dependency object.
    * If there is no object with access authority, an [[ IllegalAccessException ]] occurs.
    *
    * @tparam T Injectable type.
    * @return
    */
  def inject[T: TypeTag]: T = {
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
}
