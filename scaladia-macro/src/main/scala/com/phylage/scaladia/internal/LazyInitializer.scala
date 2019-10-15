package com.phylage.scaladia.internal

import com.phylage.scaladia.container.{CanBeContainer, Container}
import com.phylage.scaladia.exception.{DIAutoInitializationException, InjectDefinitionException}
import com.phylage.scaladia.injector.InjectionPool
import com.phylage.scaladia.injector.scope.IndexedSymbol
import com.phylage.scaladia.provider.{Accessor, Lazy}

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  import c.universe._

  def lazyInjection[T: c.WeakTypeTag](ctn: Tree, ip: Tree, access: Tree): Expr[Lazy[T]] = {

    val ctnExpr = c.Expr[Container](ctn)

    val injectionRf = reify[T] {
      injection[T](ctn, ip, access).splice match {
        case x: T with CanBeContainer[Container] =>
          x._cntMutation = ctnExpr.splice
          x
        case x => x
      }
    }

    val typName = c.Expr {
      c.reifyRuntimeClass(weakTypeOf[T])
    }

    reify[Lazy[T]] {
      new Lazy[T] {
        private[this] val mediation: CntMediateOnce[T] = CntMediateOnce.empty

        def _provide: T = try {
          mediation.getOrElse(ctnExpr.splice, {
            val r = injectionRf.splice
            mediation += ctnExpr.splice -> r
            r
          })
        } catch {
          case e: Throwable =>
            throw new DIAutoInitializationException(s"${typName.splice} or its internal initialize failed.", e)
        }
      }
    }
  }

  private[this] def injection[T: c.WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {

    val ttagExpr = c.Expr {
      c.reifyRuntimeClass(weakTypeOf[T])
    }

    val mayBeInjection = reify {
      c.Expr[Container](ctn).splice.find[T, Accessor[_]](
        c.Expr[Accessor[_]](access).splice
      )
    }

    reify {
      mayBeInjection.splice orElse {
        applymentFunction[T](ctn, ip).splice
          .toVector
          .sortBy(_.priority)(Ordering.Int.reverse)
          .headOption
          .map(_.value)
      } getOrElse {
        throw new InjectDefinitionException(s"Cannot found ${ttagExpr.splice} implementations.")
      }
    }
  }

  private[this] def applymentFunction[T: WeakTypeTag](cnt: Tree, ip: Tree): c.Expr[Set[IndexedSymbol[T]]] = {
    reify {
      c.Expr[InjectionPool](ip).splice.collect[T].apply(c.Expr[Container](cnt).splice)
    }
  }
}