package refuel.container.provider

import refuel.container.provider.Lazy
import refuel.container.Container

import scala.language.implicitConversions

/**
  * Provide internal instance Lazy[T].
  * Once injected, the object is persisted.
  * However, if a request from a different container instance occurs, it may be searched again.
  *
  * @param variable Stored dependency object.
  * @tparam X Variable type
  * @return
  */
given _implicitProviding[X]: scala.Conversion[Lazy[X], X] = {
  _._provide
}

given _implicitNestedProviding[X]: scala.Conversion[Lazy[Lazy[X]], X] = {
  _._provide._provide
}

