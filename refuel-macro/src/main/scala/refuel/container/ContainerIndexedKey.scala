package refuel.container

import scala.reflect.runtime.universe._

object ContainerIndexedKey {
  def apply[T: WeakTypeTag]: scala.Symbol = apply(weakTypeOf[T])
  def apply(value: Type): scala.Symbol    = Symbol.apply(value.toString)
}
