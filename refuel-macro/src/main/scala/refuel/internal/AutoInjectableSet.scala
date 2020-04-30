package refuel.internal

import refuel.injector.AutoInject

import scala.reflect.macros.blackbox

class AutoInjectableSet[C <: blackbox.Context](val c: C) {

  private[this] final val InjectableTag = c.weakTypeOf[AutoInject]

  def filterModuleSymbols[T](
      symbols: AutoInjectableSymbols[c.type]
  )(implicit wtt: c.WeakTypeTag[T]): Vector[c.Symbol] = {
    symbols.modules
      .filter { x => x.typeSignature <:< InjectableTag && x.typeSignature <:< wtt.tpe }
      .asInstanceOf[Set[c.Symbol]]
      .toVector
      .sortBy[String](_.fullName)
  }

  def filterClassSymbol[T](symbols: AutoInjectableSymbols[c.type])(implicit wtt: c.WeakTypeTag[T]): Vector[c.Symbol] = {
    symbols.classes
      .filter { x => x.asClass.toType <:< wtt.tpe }
      .asInstanceOf[Set[c.Symbol]]
      .toVector
      .sortBy[String](_.fullName)
  }

}
