package com.phylage.scaladia.internal

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.exception.InjectDefinitionException

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def lazyInit[T: C#WeakTypeTag](fire: c.Expr[Unit], ctn: Tree, access: c.Tree): Tree = {
    q"""
       new com.phylage.scaladia.provider.Lazy[${weakTypeOf[T]}] {
         lazy val provide: ${weakTypeOf[T]} = ${injection[T](ctn, fire, access)}
       }
     """
  }

  def injection[T: C#WeakTypeTag](ctn: Tree, fire: c.Expr[Unit], access: c.Tree): Expr[T] = {
    val tag = weakTypeOf[T]

    val mayBeInjection = c.Expr[Option[T]](
      q"""
         ${c.Expr[Container](ctn)}.find[$tag]($access)
       """
    )

    c.Expr[T] {
      q"""
       $mayBeInjection orElse {
         $fire
         $mayBeInjection
       } getOrElse {
         throw new com.phylage.scaladia.exception.InjectDefinitionException(s"Cannot found " + ${tag.typeSymbol.fullName} + " implementation.")
       }
     """
    }
  }
}