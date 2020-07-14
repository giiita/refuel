package refuel

import refuel.container.Container

import scala.language.implicitConversions

package object provider {

  /**
    * Provide internal instance Lazy[T].
    * Once injected, the object is persisted.
    * However, if a request from a different container instance occurs, it may be searched again.
    *
    * @param variable Stored dependency object.
    * @tparam X Variable type
    * @return
    */
  implicit def _implicitProviding[X](variable: Lazy[X])(implicit ctn: Container): X = {
    variable._provide
  }

  implicit def _implicitNestedProviding[X](variable: Lazy[Lazy[X]])(implicit ctn: Container): X = {
    variable._provide
  }
}
