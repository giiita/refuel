package com.github.giiita.injector

import com.github.giiita.`macro`.Macro._

object ContainerMacro {
  def pickup[T]: T = macro pickup_impl[T]
  def export[T](value: T): Unit = macro export_impl[T]
}
