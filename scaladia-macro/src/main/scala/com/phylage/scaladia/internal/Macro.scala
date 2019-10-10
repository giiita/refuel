package com.phylage.scaladia.internal

import com.phylage.scaladia.provider.Lazy

import scala.reflect.macros.blackbox


object Macro {

  def reifyClasspathInjectables[T: c.WeakTypeTag](c: blackbox.Context)(fun: c.Tree)(ctn: c.Tree, ip: c.Tree): c.Expr[T] = {
    new LazyInitializer[c.type](c).classpathRepooling[T](fun, ctn, ip)
  }

  def lazyInject[T: c.WeakTypeTag](c: blackbox.Context)(ctn: c.Tree, ip: c.Tree, access: c.Tree): c.Expr[Lazy[T]] = {
    new LazyInitializer[c.type](c).lazyInit[T](
      ctn,
      ip,
      access
    )
  }

  def diligentInject[T: c.WeakTypeTag](c: blackbox.Context)(ctn: c.Tree, ip: c.Tree, access: c.Tree): c.Expr[T] = {
    new LazyInitializer[c.type](c).diligentInit[T](
      ctn,
      ip,
      access
    )
  }

  def scratch(c: blackbox.Context): c.Tree = {
    import c.universe._
    q"""
      throw new com.phylage.scaladia.exception.UnExceptedOperateException("If you have already authorized any instance, you can not authorize new types.")
     """
  }
}
