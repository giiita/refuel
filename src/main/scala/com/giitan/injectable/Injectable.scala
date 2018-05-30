package com.giitan.injectable

import com.giitan.scope.Scope.ScopeType

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.universe._

trait Injectable[T] {

  // Registed inject type.
  val tipe: Type
  // Injection value.
  val applier: T
  // Accessible type.
  val scope: ListBuffer[ScopeType] = ListBuffer.empty

  /**
    * Append accessible type.
    */
  def +=(c: ScopeType): Injectable[T] = {
    scope += c
    this
  }

  /**
    * Clear accessible type.
    */
  def clear: Unit = this.scope.clear()

  /**
    * Be able to access global scope.
    *
    * @return
    */
  def isGlobaly: Boolean = scope.isEmpty

  /**
    * Be able to access request type.
    *
    * @param request Requesting type.
    * @return
    */
  def canAccess(request: ScopeType): Boolean = scope.exists { r =>
    r.isAssignableFrom(request)
  }
}
