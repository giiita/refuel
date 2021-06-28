package refuel.inject

import scala.reflect.macros.blackbox

object InjectableSymbolHandler {
  def filterSymbols(c: blackbox.Context)(
      symbols: Set[c.Symbol]
  )(wtt: c.Type): Vector[c.Symbol] = {
    symbols
      .filter {
        case x if x.isModule =>
          x.typeSignature <:< wtt
        case x if x.isClass =>
          x.asClass.toType <:< wtt
        case _ => false
      }
      .toVector
      .sortBy[String](_.fullName)
  }
}
