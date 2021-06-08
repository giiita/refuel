package refuel.container

import scala.language.implicitConversions

trait DependencyPoolRef[C <: Container] {
  me =>
  /* Container instance */
  var __refuel_cRef: Option[C] = None
  implicit def __refuel_c: Container = __refuel_cRef.getOrElse(
    throw UninitializedFieldError("Uninitialized container")
  )
}
