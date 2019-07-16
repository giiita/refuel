package com.phylage.scaladia.internal

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.injector.{InjectionPool, InjectionType}
import com.phylage.scaladia.provider.Lazy

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def lazyInit[T: C#WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[Lazy[T]] = {
    val tag = weakTypeOf[T]
    c.Expr[Lazy[T]](
      q"""
        {
          ${publish(ip)}
          ${c.Expr[InjectionPool](ip)}.collect[$tag].map(_.apply())
          new com.phylage.scaladia.provider.Lazy[$tag] {
            def provide: $tag = {
              try {
                ${injection[T](ctn, ip, access)}
              } catch {
                case e: java.lang.Throwable =>
                  throw new com.phylage.scaladia.exception.DIAutoInitializationException(${tag.typeSymbol.fullName} + " or its internal initialize failed.", e)
              }
            }
          }
        }
       """
    )
  }

  def publish(ip: Tree): Expr[Unit] = {
    c.Expr(
      q"""
        ${c.Expr[InjectionPool](ip)}.pool(
          () => $scrapeInjectionTypes
        )
      """
    )
  }

  def classpathRepooling[T: C#WeakTypeTag](fun: Tree, ip: Tree): Expr[T] = {
    c.Expr(
      q"""
         {
        ${publish(ip)}
        $fun
         }
      """
    )
  }

  def diligentInit[T: C#WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {
    val tag = weakTypeOf[T]
    c.Expr[T](
      q"""
         {
           ${publish(ip)}
           ${c.Expr[InjectionPool](ip)}.collect[$tag].map(_.apply())
           ${injection[T](ctn, ip, access)}
         }
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
       $mayBeInjection getOrElse {
         throw new com.phylage.scaladia.exception.InjectDefinitionException(s"Cannot found " + ${tag.typeSymbol.fullName} + " implementation.")
       }
     """
    }
  }



  def scrapeInjectionTypes: c.Expr[Iterable[InjectionType[_]]] = {
    import c.universe._
    new InjectionCompound[c.type](c).build(
      AutoDIExtractor.collectApplyTarget[c.type, Container](c)(weakTypeTag[Container])
    )
  }
}