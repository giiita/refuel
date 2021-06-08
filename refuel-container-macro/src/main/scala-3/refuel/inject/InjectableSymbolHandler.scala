package refuel.inject

import refuel.container.macros.internal.tools.LowLevelAPIConversionAlias

import scala.quoted._

object InjectableSymbolHandler extends LowLevelAPIConversionAlias {
  def filterTargetSymbols[T: Type](using q: Quotes)(symbols: Iterable[q.reflect.TypeTree]): Iterable[q.reflect.TypeTree] = {
    symbols.filter {
      _.tpe <:< TypeT_TypeRepr[T]
    }.toList.sortBy[String](_.symbol.fullName)
  }
}
