package com.phylage.scaladia.internal

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.{InjectionPool, InjectionType}
import com.phylage.scaladia.provider.Lazy

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def lazyInit[T: C#WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[Lazy[T]] = {

    c.Expr[Lazy[T]](
      q"""
        {
         ${publish(ip)}
          new com.phylage.scaladia.provider.Lazy[${weakTypeOf[T]}] {
            lazy val provide: ${weakTypeOf[T]} = try {
              ${injection[T](ctn, ip, access)}
            } catch {
              case e: java.lang.Throwable =>
                throw new com.phylage.scaladia.exception.DIAutoInitializationException(${weakTypeOf[T].typeSymbol.fullName} + " or its internal initialize failed.", e)
            }
          }
        }
       """
    )
  }

  def diligentInit[T: C#WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {
    injection[T](ctn, ip, access)
  }

  def publish(ip: Tree): Expr[() => Unit] = {
    c.Expr(
      q"""
        import scala.language.experimental.macros
        def _scrp(): Iterable[com.phylage.scaladia.injector.InjectionType[_]] = macro com.phylage.scaladia.internal.Macro.scrapeInjectionTypes
        ${c.Expr[InjectionPool](ip)}.pool(
          () => _scrp()
        )
      """
    )
  }

  def injection[T: C#WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {
    val tag = weakTypeOf[T]

    val mayBeInjection = c.Expr[Option[T]](
      q"""
         ${c.Expr[Container](ctn)}.find[$tag]($access)
       """
    )

    c.Expr[T] {
      q"""
       $mayBeInjection orElse {
         val _pool = ${c.Expr[InjectionPool](ip)}
         _pool.collect[$tag].map(_.apply())
         $mayBeInjection
       } getOrElse {
         throw new com.phylage.scaladia.exception.InjectDefinitionException(s"Cannot found " + ${tag.typeSymbol.fullName} + " implementation.")
       }
     """
    }
  }
}