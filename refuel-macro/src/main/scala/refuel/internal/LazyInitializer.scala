package refuel.internal

import refuel.container.{Container, ContainerIndexedKey}
import refuel.exception.InjectDefinitionException
import refuel.injector.InjectionPool
import refuel.injector.InjectionPool.LazyConstruction
import refuel.internal.di.{InjectionCands, SymbolExprGenerator}
import refuel.provider.{Accessor, Lazy}

import scala.reflect.macros.blackbox

class LazyInitializer[C <: blackbox.Context](val c: C) {

  private[this] type NonEmptySeq[A] = Seq[A]

  import c.universe._

  def lazyInit[T: c.WeakTypeTag](ip: Tree, access: Tree): Expr[Lazy[T]] = {
    val injectionRf = c.Expr[Container => T](
      q"""
         (ctn: refuel.container.Container) => ${injection[T](q"ctn", ip, access)} match {
           case x: refuel.injector.Injector =>
             x._cntRef = ctn
             x
           case x => x
         }
       """
    )

    reify[Lazy[T]] {
      new Lazy[T] {
        def _provide(implicit ctn: Container): T = injectionRf.splice.apply(ctn)
      }
    }
  }

  def injection[T: c.WeakTypeTag](ctn: Tree, ip: Tree, access: c.Tree): Expr[T] = {

    val ttagExpr = c.Expr {
      c.reifyRuntimeClass(weakTypeOf[T])
    }

    val mayBeInjection = reify {
      c.Expr[Container](ctn)
        .splice
        .find[T, Accessor[_]](
          c.Expr[Accessor[_]](access).splice
        )
    }

    AutoDIExtractor.searchInjectionCands[c.type, T](c) match {
      case x: InjectionCands[c.type] if !x.runtime =>
        val from               = System.currentTimeMillis()
        val (priority, ranked) = x.rankingEvaluation
        c.echo(c.enclosingPosition, s"#################################### ${System.currentTimeMillis() - from}")
        val rankingEvaluation = new SymbolExprGenerator[c.type](c).generateExpr[T](ranked)
        reify {
          mayBeInjection.splice getOrElse {
            ContainerIndexedKey.apply[T].synchronized {
              mayBeInjection.splice getOrElse {
                c.Expr[Container](ctn)
                  .splice
                  .createIndexer[T](rankingEvaluation.splice, priority.splice)
                  .indexing()
                  .value
              }
            }
          }
        }
      case x: InjectionCands[c.type] =>
        c.echo(c.enclosingPosition, s"Ranking from [${x.cands.map(_.name).mkString(",")}] and runtime classpath.")
        reify {
          mayBeInjection.splice getOrElse {
            applymentFunction[T](ctn, ip).splice match {
              case (p, f) if f.size == 1 =>
                ContainerIndexedKey.apply[T].synchronized {
                  mayBeInjection.splice getOrElse {
                    c.Expr[Container](ctn).splice.createIndexer[T](f.head(p).value, p).indexing().value
                  }
                }
              case (p, f) =>
                val x = f.map(_.apply(p).tag)
                throw new InjectDefinitionException(
                  s"Invalid dependency definition of ${ttagExpr.splice}. There must be one automatic injection of inject[T] per priority. But found [${x
                    .mkString(", ")}]"
                )
            }
          }
        }
    }
  }

  private[this] def applymentFunction[T: WeakTypeTag](cnt: Tree, ip: Tree): c.Expr[LazyConstruction[T]] = {
    reify {
      c.Expr[InjectionPool](ip)
        .splice
        .collect[T](c.Expr[Class[T]](c.reifyRuntimeClass(weakTypeOf[T])).splice)
        .apply(c.Expr[Container](cnt).splice) getOrElse {
        throw new InjectDefinitionException(s"Cannot found ${c.Expr {
          c.reifyRuntimeClass(weakTypeOf[T])
        }.splice} implementations.")
      }
    }
  }
}
