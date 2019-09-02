package com.phylage.scaladia.runtime

import scala.reflect.runtime.universe._

private[scaladia] object RuntimeAutoInjectableSymbols {
  def empty: RuntimeAutoInjectableSymbols = {
    RuntimeAutoInjectableSymbols(Set.empty, Set.empty)
  }
}

private[scaladia] case class RuntimeAutoInjectableSymbols(modules: Set[ModuleSymbol], classes: Set[ClassSymbol]) {
  def add(ms: Set[ModuleSymbol], cs: Set[ClassSymbol]): RuntimeAutoInjectableSymbols = {
    RuntimeAutoInjectableSymbols(modules ++ ms, classes ++ cs)
  }

  def ++(that: RuntimeAutoInjectableSymbols): RuntimeAutoInjectableSymbols = {
    copy(modules ++ that.modules, classes ++ that.classes)
  }
}