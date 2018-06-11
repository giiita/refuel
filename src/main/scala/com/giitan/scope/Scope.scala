package com.giitan.scope

import com.giitan.container._
import com.giitan.injector.Injector
import com.giitan.scope.Scope.ScopeType

import scala.reflect._
import runtime.universe._
import scala.collection.mutable.ListBuffer

object Scope {
  type ScopeType = Class[_]

  def apply[X: TypeTag](value: X): Scope[X] = new Scope[X] {
    val tt: TypeTag[X] = typeTag[X]
    val instance: X = value
  }
}

trait Scope[X] extends Injector {

  /**
    * Super class of injected object
    *
    * @return
    */
  val tt: TypeTag[X]
  val instance: X
  val acceptedScope: ListBuffer[ScopeType] = ListBuffer.empty

  /**
    * Extend the allowed type.
    * Only that dynamic class.
    *
    * @param cls Allowed object.
    * @tparam A Allowed type.
    * @return
    */
  def accept[A: TypeTag](cls: A): Scope[X] = {
    acceptedScope += cls.getClass
    this
  }

  /**
    * Extend the allowed type.
    * Only that type.
    *
    * @tparam A Allowed type.
    * @return
    */
  def accept[A: ClassTag]: Scope[X] = {
    acceptedScope += classTag[A].runtimeClass
    this
  }

  /**
    * Allow access to all types.
    * Default this.
    */
  def acceptedGlobal(): Scope[X] = {
    this.acceptedScope.clear()
    this
  }

  def indexing(): Unit = {
    inject[Indexer].indexing(tt, instance, this.acceptedScope)
  }
}
