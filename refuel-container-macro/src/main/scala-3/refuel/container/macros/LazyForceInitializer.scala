package refuel.container.macros

import refuel.container.macros.internal.DependencyRankings.ValDefModule_Expr
import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias
import refuel.container.macros.internal.{DependencyRankings, StaticDependencyExtractor}
import refuel.container.provider.restriction.SymbolRestriction
import refuel.container.provider.{Accessor, Lazy, TypedAcceptContext}
import refuel.container.{Container, DependencyPoolRef, IndexedKey}
import refuel.inject.InjectionPriority
import sun.reflect.generics.tree.TypeTree

import scala.annotation.tailrec
import scala.quoted._

object LazyForceInitializer extends LowLevelAPIConversionAlias {

  def init[T: Type](using q: Quotes): Expr[Lazy[T]] = {
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
        val __refuel_result = ${forceInitInject[T](container, access)}
        __refuel_result match {
          case value: DependencyPoolRef[_] =>
            value.asInstanceOf[DependencyPoolRef[Container]].__refuel_cRef = Some(ctn)
          case _ =>
          case null =>
        }
        __refuel_result
      }
    }

    '{
      Lazy[T](${injectionRf}.apply(${container}))
    }
  }

  def forceInitInject[T: Type](cnt: Expr[Container], access: Expr[Accessor[_]])(using q: Quotes): Expr[T] = {
    import q.reflect._
    val key = IndexedKey.from[T]

    val exists = '{
      ${cnt}.find[T, Accessor[_]](${key}, ${access})
    }

    val candidates = StaticDependencyExtractor.searchInjectionCandidates[T]
    DependencyRankings(candidates).fold(
      report.throwError(
        s"Can't find a dependency registration of ${q.reflect.TypeTree.of[T].symbol.fullName}."
      )
    ) {
      case (priority, ranked) if ranked.size > 1 =>
        report.throwError(s"Invalid dependency definition. There must be one automatic injection per priority. But found [${ranked.map(_.symbol.fullName).mkString(", ")}]")
      case (priority, ranked) =>
        val rankedOne = DependencyRankings.generateExprOne[T](ranked.head)
        '{
          ${exists} getOrElse {
            ${key}.synchronized {
              ${exists} getOrElse {
                ${cnt}.createIndexer[T](${key}, ${rankedOne}, ${priority})
                  .indexing().value
              }
            }
          }
        }
    }
  }
}
