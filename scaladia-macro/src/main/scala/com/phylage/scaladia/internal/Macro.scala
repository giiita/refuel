package com.phylage.scaladia.internal

import com.phylage.scaladia.injector.scope.InjectableScope
import com.phylage.scaladia.provider.Lazy

import scala.reflect.macros.blackbox

object Macro {

  def lazyInject[T: c.WeakTypeTag](c: blackbox.Context)(ctn: c.Tree, access: c.Tree): c.Expr[Lazy[T]] = {
    import c.universe._

    val detections = new AutoDIExtractor[c.type](c).run[T]()

    val flushed = flushForAll[c.type, T](c)(detections)

    new LazyInitializer[c.type](c).lazyInit[T](
      c.Expr[Unit](q"{..$flushed}"),
      ctn,
      access
    )
  }

  def diligentInject[T: c.WeakTypeTag](c: blackbox.Context)(ctn: c.Tree, access: c.Tree): c.Expr[T] = {
    import c.universe._

    val detections = new AutoDIExtractor[c.type](c).run[T]()

    val flushed = flushForAll[c.type, T](c)(detections)

    new LazyInitializer[c.type](c).diligentInit[T](
      c.Expr[Unit](q"{..$flushed}"),
      ctn,
      access
    )
  }

  def scratch(c: blackbox.Context): c.Tree = {
    import c.universe._
    q"""
      throw new com.phylage.scaladia.exception.UnExceptedOperateException("If you have already authorized any instance, you can not authorize new types.")
      """
  }

  def containerSetting[T: c.WeakTypeTag](c: blackbox.Context): c.Expr[T] = {
    import c.universe._
    val detections = new AutoDIExtractor[c.type](c).run[T]()

    val flushed = flushForAll[c.type, T](c)(detections)

    reify {
      flushed.splice.sortBy(_.priority).lastOption match {
        case Some(x) => x.value
        case None => throw new Exception("Container setup failed.")
      }
    }
  }

  private[this] def flushForAll[C <: blackbox.Context, T: c.WeakTypeTag](c: C)(x: Vector[C#Symbol]): c.Expr[Seq[InjectableScope[T]]] = {
    import c.universe._

    val flushed = x.map { name =>
      c.Expr[InjectableScope[T]](q"${c.parse(name.fullName)}.flush[${weakTypeOf[T]}]")
    }
    c.Expr[Seq[InjectableScope[T]]](
      q"Seq.apply(..$flushed)"
    )
  }
}
