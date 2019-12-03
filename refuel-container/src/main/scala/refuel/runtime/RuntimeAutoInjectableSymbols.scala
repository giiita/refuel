package refuel.runtime

import scala.reflect.runtime.universe._

private[refuel] case class RuntimeAutoInjectableSymbols(modules: Set[ModuleSymbol], classes: Set[ClassSymbol])