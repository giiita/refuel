package refuel.internal.di

import refuel.domination.InjectionPriority.Default
import refuel.domination.{Inject, InjectionPriority}

import scala.reflect.macros.blackbox

case class InjectionCands[C <: blackbox.Context](c: C)(val cands: Vector[C#Symbol], val runtime: Boolean = false) {

  import c.universe._

  /* Injection priority config type tag */
  private[this] lazy val InjectionPriorityConfigType = c.weakTypeOf[Inject[_]]
  private[this] lazy val DefaultType                 = c.weakTypeOf[Default]

  def rankingEvaluation: (Expr[InjectionPriority], Seq[C#Symbol]) = {
    cands match {
      case _ =>
        val candsPs: Vector[(C#Type, C#Symbol)] = cands
          .map { sm =>
            val ip = sm.annotations
              .find(_.tree.tpe <:< InjectionPriorityConfigType)
              .flatMap(_.tree.tpe.typeArgs.headOption.asInstanceOf[Option[C#Type]])
              .getOrElse {
                DefaultType.dealias.asInstanceOf[C#Type]
              }
            ip -> sm
          }
        val higher = candsPs.groupBy(_._1).reduce { (a, b) =>
          val maybe = a._1.baseType(b._1.typeSymbol).orElse(b._1.baseType(a._1.typeSymbol))
          if (maybe =:= a._1) a else b
        }
        c.Expr[InjectionPriority](c.parse(higher._1.typeSymbol.fullName)) -> higher._2.map(_._2)
    }
  }
}
