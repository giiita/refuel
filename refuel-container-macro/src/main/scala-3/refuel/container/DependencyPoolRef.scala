package refuel.container

import scala.language.implicitConversions
import provider.Accessor

trait DependencyPoolRef[C <: Container] {
  me =>
  /* Container instance */
  var __refuel_cRef: Option[C] = None
  implicit def __refuel_c: C = __refuel_cRef.getOrElse(
    throw new UninitializedFieldError("Uninitialized container")
  )
}
