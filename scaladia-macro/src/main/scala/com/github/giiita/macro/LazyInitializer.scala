package com.github.giiita.`macro`

import com.github.giiita.container.Container

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def lazyInit[T: C#WeakTypeTag](fire: c.Expr[Unit], ctn: Tree, access: c.Tree): Tree = {
    q"""
       new com.github.giiita.provider.Lazy[${weakTypeOf[T]}] {
         def provide: ${weakTypeOf[T]} = ${injection[T](ctn, fire, access)}
       }
     """
  }

  def injection[T: C#WeakTypeTag](ctn: Tree, fire: c.Expr[Unit], access: c.Tree): Expr[T] = {
    val tag = weakTypeOf[T]

    val buffer = reify {
      c.Expr[Container](ctn).splice.getBuffer
    }

    val mayBeInjection = c.Expr[Option[T]](q"""
       $buffer.filter(_.accepted[$tag]($access)).sortBy(_.priority).lastOption.map(_.value.asInstanceOf[$tag])
       """
    )

    c.Expr[T] {
      q"""
       $mayBeInjection orElse {
         println("Primary foundation failed.")
         $fire
         $mayBeInjection
       } getOrElse {
         throw new Exception(s"Cannot found" + ${tag.typeSymbol.fullName} + "implementation.")
       }
     """
    }
  }
}