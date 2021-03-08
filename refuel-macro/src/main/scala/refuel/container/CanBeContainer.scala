package refuel.container

import refuel.injector.InjectionPool
import refuel.provider.Accessor

import scala.language.implicitConversions

trait CanBeContainer[C <: Container] {
  me =>
  /* Container instance */
  implicit var _cntRef: C

  /**
    * This refers to itself.
    * When injecting, check if this Accessor is authorized.
    *
    * @return
    */
  protected implicit def _someoneNeeds: Accessor[_] = Accessor(me)

  /**
    * Implicitly injection pool
    *
    * @return
    */
  protected implicit def _ijp: InjectionPool
}
