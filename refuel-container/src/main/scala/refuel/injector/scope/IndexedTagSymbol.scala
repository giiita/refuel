package refuel.injector.scope

import scala.reflect.runtime.universe._

private[refuel] abstract class IndexedTagSymbol[T](val tag: Type) extends IndexedSymbol[T]
