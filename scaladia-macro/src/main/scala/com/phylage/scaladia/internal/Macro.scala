package com.phylage.scaladia.internal

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.InjectionType
import com.phylage.scaladia.provider.Lazy

import scala.reflect.macros.blackbox


object Macro {

  def scrapeInjectionTypes(c: blackbox.Context)(): c.Expr[Iterable[InjectionType[_]]] = {
    import c.universe._
    new InjectionCompound[c.type](c).build(
      AutoDIExtractor.collectApplyTarget[c.type, Container](c)(weakTypeTag[Container])
    )
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
