package com.github.giiita

import scala.reflect.macros.blackbox

package object `macro` {
  def lazyInit[T](c: blackbox.Context)(fire: c.Expr[T], tag: c.universe.Type): c.Tree = {
    import c.universe._
    q"""
         $fire
         new com.github.giiita.provider.Lazy[$tag] {
           def provide: $tag = ${xx(c)(tag)}
         }
        """
  }

  def xx[T](c: blackbox.Context)(tag: c.universe.Type): c.Expr[T] = {
    import c.universe._
    c.Expr[T] {
      q"""
         implicitly[com.github.giiita.container.Container].getBuffer.collect {
           case x if x.isSameAs[$tag] => x
         }.sortBy(_.priority).lastOption match {
           case Some(x) => x.value.asInstanceOf[$tag]
           case None => throw new Exception("Cannot found " + ${tag.typeSymbol.fullName} + " implementation.")
         }
       """
    }
  }
}
