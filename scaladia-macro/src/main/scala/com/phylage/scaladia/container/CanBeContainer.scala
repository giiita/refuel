package com.phylage.scaladia.container

import com.phylage.scaladia.injector.InjectionPool
import com.phylage.scaladia.provider.Accessor

import scala.language.implicitConversions

trait CanBeContainer[C <: Container] {
  me =>
  /* Container instance */
  implicit var _cntMutation: C

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
