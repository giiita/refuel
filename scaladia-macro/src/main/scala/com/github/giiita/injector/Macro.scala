package com.github.giiita.injector

import scala.reflect.macros.blackbox

object Macro {
  def inject[T: c.WeakTypeTag](c: blackbox.Context)(value: c.Tree): c.Tree = {
    import c.universe._

    val ll = c.weakTypeOf[T].typeSymbol.

    q"""
       Hoge.TagFactory.add(${ll.toString})
     """
  }
}
