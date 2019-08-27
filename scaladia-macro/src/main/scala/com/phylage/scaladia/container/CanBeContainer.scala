package com.phylage.scaladia.container

import com.phylage.scaladia.injector.InjectionPool
import com.phylage.scaladia.provider.{Accessor, Lazy}

import scala.language.implicitConversions

trait CanBeContainer[C <: Container] { me =>
  /* Container instance */
  implicit var _cntMutation: C

  /**
    * Provide internal instance Lazy[T].
    * Once injected, the object is persisted.
    * However, if a request from a different container instance occurs, it may be searched again.
    *
    * @param variable Stored dependency object.
    * @tparam X Variable type
    * @return
    */
  implicit def _implicitProviding[X](variable: Lazy[X]): X = {
    variable._provide
  }

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
