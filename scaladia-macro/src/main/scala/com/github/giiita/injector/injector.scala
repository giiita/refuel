package com.github.giiita.injector

package object injector {
  def exe[T](value: String): Any = macro Macro.inject[T]
}
