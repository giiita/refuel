package refuel.internal

import scala.reflect.macros.blackbox

object AutoInjectableSymbols {
  def empty[C <: blackbox.Context](c: C): AutoInjectableSymbols[C] = {
    AutoInjectableSymbols[C](c)(Set.empty, Set.empty)
  }
}

case class AutoInjectableSymbols[C <: blackbox.Context](c: C)(val modules: Set[C#Symbol], val classes: Set[C#Symbol]) {
  def add(ms: Vector[C#Symbol], cs: Vector[C#Symbol]): AutoInjectableSymbols[C] = {
    AutoInjectableSymbols(c)(modules ++ ms, classes ++ cs)
  }

  def ++(that: AutoInjectableSymbols[C]): AutoInjectableSymbols[C] = {
    copy(c)(modules ++ that.modules, classes ++ that.classes)
  }
}
