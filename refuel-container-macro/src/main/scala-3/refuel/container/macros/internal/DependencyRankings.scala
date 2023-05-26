package refuel.container.macros.internal

import refuel.container.Container
import refuel.container.macros.internal.DependencyRankings.isDependencyPoolRef
import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias
import refuel.container.provider.Accessor
import refuel.inject.InjectionPriority.Default
import refuel.inject.Types.LocalizedContainer
import refuel.inject.{AutoInject, Inject, InjectableTag, InjectionPriority, InjectableSymbolHandler}

import scala.annotation.tailrec
import scala.quoted._

object DependencyRankings extends LowLevelAPIConversionAlias {
  /* Injection priority config type tag */
  private[this] def InjectionPriorityConfigType(using q: Quotes): q.reflect.TypeRepr = q.reflect.TypeRepr.of[Inject[?]]
  private[this] def DefaultType(using q: Quotes): q.reflect.TypeRepr                 = q.reflect.TypeRepr.of[refuel.inject.InjectionPriority.Default]

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
    build[T](samePriority)
  }

  def generateExpr[T: Type](using q: Quotes)(samePriorities: Iterable[q.reflect.TypeTree]): Expr[Iterable[T]] = {
    import q.reflect._
    report.info(s"${samePriorities.map(_.symbol.fullName).mkString(" & ")} will be used.")
    Expr.ofSeq(
      samePriorities.map(build[T]).toSeq
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

  private[this] def constructInjectionTerm(using q: Quotes)(target: q.reflect.Symbol): q.reflect.Term = {
    import q.reflect._
    target.tree match {
      case ValDef(_, tree, _) =>
        Select.unique(
          Select.unique(This(isDependencyPoolRef(q.reflect.Symbol.spliceOwner)), "inject").appliedToType(tree.tpe).asExpr.asTerm,
          "_provide"
        ).asExpr.asTerm
    }
  }

  private[this] def implicitInjectionTerm(using q: Quotes)(target: q.reflect.TypeRepr, typeBound: Map[String, q.reflect.TypeRepr]): q.reflect.Term = {
    import q.reflect._
    Implicits.search(
      target match {
        case AppliedType(ths, args) => {
          ths.appliedTo(args.flatMap(x => typeBound.get(x.typeSymbol.name)))
        }
        case e => {
          e
        }
      }
    ) match {
      case iss: ImplicitSearchSuccess => iss.tree.asExpr.asTerm
      case e: ImplicitSearchFailure => report.throwError(e.explanation)
    }
  }

  private[this] def build[T: Type](using q: Quotes)(target: q.reflect.TypeTree): Expr[T] = {
    import q.reflect._
    val ts = target.symbol
    if (ts.flags.is(Flags.Module)) {
      This(ts).asExpr.asExprOf[T]
    } else if (ts.isValDef) {
      ValDefModule_Expr(ts).asExprOf[T]
    } else {
      val typeConcreteMap = InjectableSymbolHandler.typeConcreteMap[T]
      val typeApplied = List.newBuilder[List[TypeRepr]]
      val constructs = if (ts.primaryConstructor.isDefDef) {
        ts.primaryConstructor.tree match {
          case q.reflect.DefDef(a, b, c, d) => {
            b.flatMap {
              case trc: TypeParamClause => {
                typeApplied.addOne(
                  trc.params.flatMap { x =>
                    typeConcreteMap.get(x.symbol.name)
                  }
                )
                None
              }
              case trc: TermParamClause if trc.isImplicit || trc.isGiven => {
                Some(
                  trc.params.map { x =>
                    implicitInjectionTerm(x.tpt.tpe, typeConcreteMap)
                  }
                )
              }
              case trc => Some(trc.params.map(x => constructInjectionTerm(x.symbol)))
            }
          }
        }
      } else {
        ts.primaryConstructor.paramSymss.map(_.map(constructInjectionTerm))
      }
      val typeAppliedResult = typeApplied.result()
      
      if (typeAppliedResult.isEmpty) {
        Select.unique(
          New(TypeIdent(ts)),
          "<init>"
        ).appliedToArgss(constructs).asExprOf[T]
      } else {
        typeAppliedResult.foldLeft[Term](
          Select.unique(
            New(TypeIdent(ts)),
            "<init>"
          )
        ) { case (a, b) =>
          a.appliedToTypes(b)
        }.appliedToArgss(constructs).asExprOf[T]
      }
    }
  }
}
