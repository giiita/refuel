package refuel.inject

import scala.reflect.macros.blackbox

object InjectableSymbolHandler {
  def filterSymbols(c: blackbox.Context)(
      symbols: Set[c.Symbol]
  )(wtt: c.Type): Vector[c.Symbol] = {
    import c.universe._
    symbols
      .filter {
        case x if x.isModule =>
          x.typeSignature <:< wtt
        case x if x.isClass =>
          x.asClass.toType <:< wtt || {
            x.asClass.typeParams.nonEmpty &&
              x.asClass.toType.baseType(wtt.typeSymbol).typeSymbol != NoSymbol &&
              x.asClass.typeParams.forall(p => wtt.typeSymbol.asClass.typeParams.exists(_.name == p.name))
          }
        case _ => false
      }
      .toVector
      .sortBy[String](_.fullName)
  }
}
