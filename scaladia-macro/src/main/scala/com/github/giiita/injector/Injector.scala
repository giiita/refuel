package com.github.giiita.injector

import scala.language.experimental.macros
import com.github.giiita.`macro`.Macro
import com.github.giiita.provider.Lazy

trait Injector {
  def inject[T]: Lazy[T] = macro Macro.inject[T]

  import scala.language.implicitConversions

  /**
    * Provide dependency.
    *
    * @param variable Stored dependency object.
    * @tparam X
    * @return
    */
  implicit def provide[X](variable: Lazy[X]): X = variable.provide
}
