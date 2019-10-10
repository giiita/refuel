package com.phylage.scaladia.injector

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.scope.IndexedSymbol

import scala.reflect.runtime.universe._

object AutoInject {
  val DEFAULT_INJECTION_PRIORITY = 1000
}

/**
  * It is a basic automatic injection interface.
  * Change use [[com.phylage.scaladia.injector.AutoInjectCustomPriority]] or any Injector that inherits AutoInjectCustomPriority
  *
  * @tparam T Type to register
  */
trait AutoInject[T] extends AutoInjectable[T] with Injector { me: T =>

  private[scaladia] val injectionPriority = AutoInject.DEFAULT_INJECTION_PRIORITY

  /**
    * Called when indexing into a container.
    * There is no need to call it intentionally.
    *
    * @param c container instance
    * @param wtt weak type tag of self
    * @return
    */
  private[scaladia] def flush(c: Container)(implicit wtt: WeakTypeTag[T]): IndexedSymbol[T] = {
    c.createIndexer[T](
      me,
      injectionPriority,
      c.lights
    )(wtt).indexing()
  }
}
