package refuel.injector.scope

trait TypedAcceptContext[-T] {
  def accepted: IndexedSymbol[_] => T => Boolean
}
