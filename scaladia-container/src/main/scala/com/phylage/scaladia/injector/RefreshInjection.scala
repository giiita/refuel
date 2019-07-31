package com.phylage.scaladia.injector

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.internal.Macro

/**
  * Used to repool auto-injectable objects present in the current classpath.
  *
  * Basic injection needs to be executed in the module to be started first.
  * The declaration injected in the library has already completed macro expansion.
  *
  * That is, when a third-party object accessed without injection tries to inject
  * someone for the first time, higher priority objects in the current classpath may be ignored.
  *
  * At that time, by inheriting RefreshInjection at a higher level, AutoInjectable in the class path will be pooled again.
  */
trait RefreshInjection extends Injector {
  /**
    * Instantiates injectable objects and updates the pool with the current classpath.
    * For example, if this is {{{inject[Runner].execute}}}, you do not need reify.
    *
    * {{{
    * object Main extends App with RefreshInjection {
    *   reify {
    *     // Runner is an external dependency module.
    *     // The call to inject[X] internally, the implementation class of X is in the first-party module.
    *     println(Runner.execute)
    *   }
    * }
    * }}}
    *
    * @param fun any function
    * @tparam T result type
    * @return
    */
  def reify[T](fun: T)(implicit ctn: Container, ip: InjectionPool): T = macro Macro.reifyClasspathInjectables[T]
}