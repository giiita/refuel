package com.phylage.scaladia.injector.scope

trait TypedAcceptContext[-T] {
  def accepted: IndexedSymbol[_] => T => Boolean
}
