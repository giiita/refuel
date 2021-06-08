package refuel.container.macros.internal.tools

import scala.quoted._

trait LowLevelAPIConversionAlias {
  def Symbol_Expr(using q: Quotes)(from: q.reflect.Symbol): Expr[Any] = {
    import q.reflect._
    from.tree.asExpr
  }
  def Symbol_TypeRepr(using q: Quotes)(from: q.reflect.Symbol): q.reflect.TypeRepr = {
    import q.reflect._
    from.tree match {
//      case 
      case _ => Symbol_Expr(from).asTerm.tpe
    }
    Symbol_Expr(from).asTerm.tpe
  }
  def Symbol_TypeT(using q: Quotes)(from: q.reflect.Symbol): Type[?] = {
    import q.reflect._
    Symbol_TypeRepr(from).asType
  }

  def TypeT_TypeRepr[T <: AnyKind: Type](using q: Quotes): q.reflect.TypeRepr = {
    import q.reflect._
    TypeRepr.of[T]
  }

  /**
   * ```
   * val valdef = q.reflect.TypeRepr.of[refuel.inject.InjectionPriority].typeSymbol.companionModule.declaredField("Default")
   * ValDefModule_Expr(valdef)
   * ```
   * @param q
   * @param from
   * @return
   */
  def ValDefModule_Expr(using q: Quotes)(from: q.reflect.Symbol): Expr[Any] = {
    import q.reflect._
    from.tree match {
      case ValDef (_, tree, _) => tree.tpe match {
        case x: TermRef =>
          Ident(x).asExpr
      }
    }
  }
}
