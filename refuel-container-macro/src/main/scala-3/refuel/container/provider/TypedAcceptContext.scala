package refuel.container.provider

import refuel.container.provider.restriction.SymbolRestriction

trait TypedAcceptContext[-T] {
  def accepted: SymbolRestriction[_] => T => Boolean
}
