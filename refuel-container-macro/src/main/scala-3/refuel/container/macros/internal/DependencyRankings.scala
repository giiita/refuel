package refuel.container.macros.internal

import refuel.container.Container
import refuel.container.macros.internal.DependencyRankings.isDependencyPoolRef
import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias
import refuel.inject.InjectionPriority.Default
import refuel.inject.Types.LocalizedContainer
import refuel.inject.{AutoInject, Inject, InjectableTag, InjectionPriority}

import scala.annotation.tailrec
import scala.quoted._

object DependencyRankings extends LowLevelAPIConversionAlias {
  /* Injection priority config type tag */
  private[this] def InjectionPriorityConfigType(using q: Quotes): q.reflect.TypeRepr = q.reflect.TypeRepr.of[Inject[?]]
  private[this] def DefaultType(using q: Quotes): q.reflect.TypeRepr                 = q.reflect.TypeRepr.of[refuel.inject.InjectionPriority.Default.type]

  def apply(using q: Quotes)(cands: Iterable[q.reflect.TypeTree]): Option[(Expr[InjectionPriority], Iterable[q.reflect.TypeTree])] = {
    import q.reflect._
    cands.map { sym =>
      {
        sym.symbol.annotations.find(_.tpe <:< InjectionPriorityConfigType)
          .flatMap { a =>
            a.tpe match {
            case q.reflect.AppliedType(_, List(x)) =>
              Some(x)
            case _ =>
              None
          }}.getOrElse({
          DefaultType
        })
      } -> sym
    }.groupBy(_._1.dealias).foldLeft[Option[(q.reflect.TypeRepr, Iterable[(q.reflect.TypeRepr, q.reflect.TypeTree)])]](None) { (a, b) =>
      a.fold(Some(b)) { prev =>
        val maybe = {
          prev._1.baseType(b._1.typeSymbol) match {
            case x if x.typeSymbol.isNoSymbol => b._1.baseType(prev._1.typeSymbol)
            case x => x
          }
        }

        Some(
          if (maybe =:= prev._1) prev else b
        )
      }
    }.flatMap {
      case (priorityGroup, pAndSyms) =>
        Some(
          {
            priorityGroup.typeSymbol.companionModule.tree match {
              case ValDef(_, tree, _) =>
                This(tree.symbol).asExpr.asExprOf[InjectionPriority]
            }
          } -> pAndSyms.map(_._2)
        )
    }

  }

  def generateExprOne[T: Type](using q: Quotes)(samePriority: q.reflect.TypeTree): Expr[T] = {
    import q.reflect._
    report.info(s"${samePriority.symbol.fullName} will be used.")
    build[T](samePriority.symbol)
  }

  def generateExpr[T: Type](using q: Quotes)(samePriorities: Iterable[q.reflect.TypeTree]): Expr[Iterable[T]] = {
    import q.reflect._
    report.info(s"${samePriorities.map(_.symbol.fullName).mkString(" & ")} will be used.")
    Expr.ofSeq(
      samePriorities.map(x => build[T](x.symbol)).toSeq
    )
  }

  @tailrec
  private[this] def isDependencyPoolRef(using q: Quotes)(sym: q.reflect.Symbol): q.reflect.Symbol = {
    sym.tree match {
      case dotty.tools.dotc.ast.Trees.TypeDef(_, _) if q.reflect.TypeIdent(sym).tpe.<:<(q.reflect.TypeTree.of[refuel.container.DependencyPoolRef[LocalizedContainer]].tpe) =>
        sym
      case _ => isDependencyPoolRef(sym.owner)
    }
  }

  private[this] def build[T: Type](using q: Quotes)(target: q.reflect.Symbol): Expr[T] = {
    import q.reflect._
    if (target.flags.is(Flags.Module)) {
      This(target).asExpr.asExprOf[T]
    } else if (target.isValDef || target.flags.is(Flags.Module)) {
      ValDefModule_Expr(target).asExprOf[T]
    } else {
      Select.overloaded(
        New(TypeIdent(target)),
        "<init>",
        Nil,
        target.primaryConstructor.paramSymss.flatMap(_.map { x =>
          x.tree match {
            case ValDef(_, tree, _) =>
              tree.tpe.asType match {
                case '[t] =>
                  Select.unique(
                    Select.unique(This(isDependencyPoolRef(q.reflect.Symbol.spliceOwner)), "inject").appliedToType(tree.tpe).asExpr.asTerm,
                    "_provide"
                  ).asExpr.asTerm
              }
          }
        })
      ).asExprOf[T]
    }
  }
}
