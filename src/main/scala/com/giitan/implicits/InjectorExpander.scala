package com.giitan.implicits

import com.giitan.injector.Injector

import scala.reflect.runtime.universe._

trait InjectorExpander {me: Injector =>
  implicit class Expand[T](t: T) {
    def as[X >: T: TypeTag]: Unit = {
      me.depends[X](t)
    }
  }
}
