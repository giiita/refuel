package refuel.container.`macro`

import refuel.inject.{Inject, InjectionPriority}

import scala.reflect.macros.blackbox

object DependencyRankings {
  def apply(c: blackbox.Context)(cands: Iterable[c.Symbol]): Option[(c.Expr[InjectionPriority], Iterable[c.Symbol])] = {
    cands match {
      case _ =>
        val candsPs: Iterable[(c.Type, c.Symbol)] = cands
          .map { sm =>
            val ip = sm.annotations
              .find(_.tree.tpe <:< InjectionPriorityConfigType(c))
              .flatMap(_.tree.tpe.typeArgs.headOption)
              .getOrElse {
                DefaultType(c).dealias
              }
            ip -> sm
          }
        val higher = candsPs.groupBy(_._1).foldLeft[Option[(c.Type, Iterable[(c.Type, c.Symbol)])]](None) { (a, b) =>
          a.fold(Some(b)) { prev =>
            val maybe = prev._1.baseType(b._1.typeSymbol).orElse(b._1.baseType(prev._1.typeSymbol))
            Some(if (maybe =:= prev._1) prev else b)
          }
        }
        higher.map { h => c.Expr[InjectionPriority](c.parse(h._1.typeSymbol.fullName)) -> h._2.map(_._2) }
    }
  }

  /* Injection priority config type tag */
  private[this] def InjectionPriorityConfigType(c: blackbox.Context) = c.weakTypeOf[Inject[_]]

  private[this] def DefaultType(c: blackbox.Context) = c.weakTypeOf[refuel.inject.InjectionPriority.Default.type]

  def generateExprOne(c: blackbox.Context)(samePriority: c.Symbol): c.Expr[_] = {
    c.info(c.enclosingPosition, s"${samePriority.fullName} will be used.", true)
    buildExprs(c)(samePriority)
  }

  def generateExpr(
      c: blackbox.Context
  )(samePriorities: Iterable[c.universe.Symbol]): c.Expr[Iterable[_]] = {
    c.info(c.enclosingPosition, s"${samePriorities.map(_.fullName).mkString(" & ")} will be used.", true)
    import c.universe._
    c.Expr[Iterable[_]](
      q"""Seq(..${samePriorities.map(x => buildExprs(c)(x)).toSeq})"""
    )
  }

  def buildExprs(c: blackbox.Context)(target: c.Symbol): c.Expr[_] = {
    if (target.isModule) {
      c.Expr(c.parse(target.fullName))
    } else {
      c.Expr {
        c.parse(
          s"""new ${target.fullName}(${target.asClass.primaryConstructor.asMethod.paramLists
            .collect {
              case curry if !curry.exists(_.isImplicit) =>
                curry.map { param => s"inject[${param.typeSignature.toString}]" }.mkString(",")
            }
            .mkString(")(")})"""
        )
      }
    }
  }
}
