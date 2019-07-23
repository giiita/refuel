package com.phylage.scaladia.internal

import com.phylage.scaladia.container.Container
import com.phylage.scaladia.exception.{DIAutoInitializationException, InjectDefinitionException}
import com.phylage.scaladia.injector.scope.InjectableScope
import com.phylage.scaladia.injector.{InjectionPool, InjectionType}
import com.phylage.scaladia.provider.{Accessor, Lazy}

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def lazyInit[T: c.WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[Lazy[T]] = {
    val injectionRf = c.Expr[T](
      q"""
         ${injection[T](ctn, ip, access)} match {
           case x: com.phylage.scaladia.injector.Injector =>
             x._cntMutation = $ctn
             x
           case x => x
         }
       """
    )

    val typName = c.Expr {
      c.reifyRuntimeClass(weakTypeOf[T])
    }

    reify[Lazy[T]] {
      new Lazy[T] {
        def _provide: T = try {
          injectionRf.splice
        } catch {
          case e: Throwable =>
            throw new DIAutoInitializationException(s"${typName.splice} or its internal initialize failed.", e)
        }
      }
    }
  }

  def publish(ip: Tree): Expr[Unit] = {
    reify {
      c.Expr[InjectionPool](ip).splice.pool {
        () => scrapeInjectionTypes.splice
      }
    }
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
    c.Expr[T](
      q"""
         {
           ${publish(ip)}
           ${applymentFunctionTree[T](ctn, ip)}
           ${injection[T](ctn, ip, access)}
         }
       """
    )
  }

  def injection[T: c.WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {

    val ttagExpr = c.Expr {
      c.reifyRuntimeClass(weakTypeOf[T])
    }

    val mayBeInjection = reify {
      c.Expr[Container](ctn).splice.find[T](
        c.Expr[Accessor[_]](access).splice
      )
    }

    reify {
      mayBeInjection.splice orElse {
        applymentFunctionTree[T](ctn, ip).splice
        mayBeInjection.splice
      } orElse {
        publish(ip).splice
        applymentFunctionTree[T](ctn, ip).splice
        mayBeInjection.splice
      } getOrElse {
        throw new InjectDefinitionException(s"Cannot found ${ttagExpr.splice} implementations.")
      }
    }
  }

  private def applymentFunctionTree[T: WeakTypeTag](cnt: Tree, ip: Tree): c.Expr[Vector[InjectableScope[T]]] = {
    reify {
      c.Expr[InjectionPool](ip).splice.collect[T].map(_.apply(c.Expr[Container](cnt).splice))
    }
  }

  def scrapeInjectionTypes: c.Expr[Iterable[InjectionType[_]]] = {
    import c.universe._
    new InjectionCompound[c.type](c).build(
      AutoDIExtractor.collectApplyTarget[c.type, Container](c)(weakTypeTag[Container])
    )
  }
}