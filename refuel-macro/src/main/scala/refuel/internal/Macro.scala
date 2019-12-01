package refuel.internal

import refuel.provider.Lazy

import scala.reflect.macros.blackbox


class Macro(val c: blackbox.Context) {

  // def reinjectPrimaryConstruction[T: c.WeakTypeTag](apl: c.Expr[T]):

  def lazyInjectDyn[T: c.WeakTypeTag](t: c.Tree)(ctn: c.Tree, ip: c.Tree, access: c.Tree): c.Expr[Lazy[T]] = {
    new LazyInitializer[c.type](c).lazyInit[T](
      ctn,
      ip,
      access
    )
  }

  def lazyInject[T: c.WeakTypeTag](ctn: c.Tree, ip: c.Tree, access: c.Tree): c.Expr[Lazy[T]] = {
    new LazyInitializer[c.type](c).lazyInit[T](
      ctn,
      ip,
      access
    )
  }

  def diligentInject[T: c.WeakTypeTag](ctn: c.Tree, ip: c.Tree, access: c.Tree): c.Expr[T] = {
    new LazyInitializer[c.type](c).diligentInit[T](
      ctn,
      ip,
      access
    )
  }

  def scratch: c.Tree = {
    import c.universe._
    q"""
      throw new refuel.exception.UnExceptedOperateException("If you have already authorized any instance, you can not authorize new types.")
     """
  }
}
