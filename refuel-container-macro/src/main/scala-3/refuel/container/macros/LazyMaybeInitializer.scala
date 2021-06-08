package refuel.container.macros

import refuel.container.macros.internal.DependencyRankings.ValDefModule_Expr
import refuel.container.macros.internal.{DependencyRankings, StaticDependencyExtractor}
import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias
import refuel.container.{Container, DependencyPoolRef, IndexedKey}
import refuel.container.provider.{Accessor, Lazy, TypedAcceptContext}
import refuel.container.provider.restriction.SymbolRestriction
import refuel.inject.InjectionPriority
import sun.reflect.generics.tree.TypeTree

import scala.annotation.tailrec
import scala.quoted._

object LazyMaybeInitializer extends LowLevelAPIConversionAlias {

  def init[T: Type](using q: Quotes): Expr[Lazy[Option[T]]] = {
    import q.reflect._
    val access: Expr[Accessor[_]] = Implicits.search(q.reflect.TypeRepr.of[Accessor[_]]) match {
      case iss: ImplicitSearchSuccess => iss.tree.asExpr.asInstanceOf[Expr[Accessor[_]]]
      case _: ImplicitSearchFailure => report.throwError("No found accessor.")
    }
    val container: Expr[Container] = Implicits.search(q.reflect.TypeRepr.of[Container]) match {
      case iss: ImplicitSearchSuccess => iss.tree.asExpr.asInstanceOf[Expr[Container]]
      case _: ImplicitSearchFailure => report.throwError("No found container reference.")
    }
    val injectionRf = '{
      (ctn: Container) => {
        ${maybeInject[T](container, access)}.map { value =>
          value match {
            case x: DependencyPoolRef[Container] =>
              x.__refuel_cRef = Some(ctn)
            case _ =>
          }
          value
        }
      }
    }

    '{
      Lazy[Option[T]](${injectionRf}.apply(${container}))
    }
  }

  def maybeInject[T: Type](cnt: Expr[Container], access: Expr[Accessor[_]])(using q: Quotes): Expr[Option[T]] = {
    import q.reflect._
    val key = IndexedKey.from[T]

    val exists: Expr[Option[T]] = '{
      ${cnt}.find[T, Accessor[_]](${key}, ${access})
    }

    val candidates = StaticDependencyExtractor.searchInjectionCandidates[T]

    DependencyRankings(candidates).fold(
      exists
    ) {
      case (priority, ranked) if ranked.size > 1 =>
        report.throwError(s"Invalid dependency definition. There must be one automatic injection per priority. But found [${ranked.map(_.symbol.fullName).mkString(", ")}]")
      case (priority, ranked) =>
        val rankedOne = DependencyRankings.generateExprOne[T](ranked.head)
        '{
          ${exists} orElse {
            ${key}.synchronized {
              ${exists} orElse {
                Some(
                  ${cnt}.createIndexer[T](${key}, ${rankedOne}, ${priority})
                    .indexing().value
                )
              }
            }
          }
        }
    }
  }
}
