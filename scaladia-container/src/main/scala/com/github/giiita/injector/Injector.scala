package com.github.giiita.injector

import com.github.giiita.`macro`.Macro
import com.github.giiita.container.{Container, ContainerStore, DefaultContainer}
import com.github.giiita.provider.{Accessor, Lazy}

trait Injector {
  me =>
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

  implicit def from: Accessor[_] = Accessor(me)

  implicit def getContainer: Container = implicitly[ContainerStore].ctn
}
