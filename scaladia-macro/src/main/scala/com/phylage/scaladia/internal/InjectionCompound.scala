package com.phylage.scaladia.internal

import com.phylage.scaladia.injector.InjectionType

import scala.reflect.macros.blackbox

class InjectionCompound[C <: blackbox.Context](val c: C) {

  import c.universe._

  def build(symbols: Iterable[C#Symbol]): c.Expr[Iterable[InjectionType[_]]] = {

    val flushed = symbols.map { name =>
      c.Expr[InjectionType[_]](
        q"""
           com.phylage.scaladia.injector.InjectionType.apply(${c.parse(name.fullName)}.flush, ${name.fullName})
         """
      )
    }
    c.Expr[Iterable[InjectionType[_]]](
      q"Vector(..$flushed)"
    )
  }
}
