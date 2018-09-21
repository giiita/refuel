package com.giitan.injector

import com.giitan.container.Indexer
import com.giitan.scope.Scope.ClassScope

import scala.reflect.runtime.universe._

trait AutoInject[X] extends Injector { me: X =>

  /**
    * Regist for DI container.
    *
    * @tparam T
    */
  private[giitan] final def registForContainer[T: TypeTag]: Unit = {
    inject[Indexer[ClassScope]].indexing[T](typeTag[T], me.asInstanceOf[T])
  }
}