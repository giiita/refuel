package com.giitan.injectable

import com.giitan.scope.Scope.ScopeType

import scala.reflect.runtime.universe._

trait Injectable[T] {

  // Registed inject type.
  val tipe: Type
  // Injection value.
  val applier: T
  // Accessible type.
  val scope: Seq[ScopeType]

  /**
    * Be able to access global scope.
    *
    * @return
    */
  def isGlobaly: Boolean = scope.isEmpty

  /**
    * Be accepted from request type.
    *
    * @param request Requesting type.
    * @return
    */
  def isAccepted(request: ScopeType): Boolean = scope.exists { r =>
    r.isAssignableFrom(request)
  }

  /**
    * Be able to access request type.
    *
    * @param request Requesting type
    * @return
    */
  def canAccessMe(request: ScopeType): Boolean = isGlobaly || isAccepted(request)
}
