package com.giitan.injectable

import com.giitan.scope.Scope.{ClassScope, ObjectScope}

import scala.reflect.runtime.universe._

trait Injectable[T, ST[_]] {

  // Registed inject type.
  val tipe: Type
  // Injection value.
  val applier: T
  // Accessible type.
  val scope: Seq[ST[_]]

  /**
    * Be accepted from request type.
    *
    * @param request Requesting type.
    * @return
    */
  def isAccepted(request: ST[_]): Boolean

  /**
    * Be able to access request type.
    *
    * @param request Requesting type
    * @return
    */
  def canAccessMe(request: ST[_]): Boolean = isGlobaly || isAccepted(request)

  /**
    * Be able to access global scope.
    *
    * @return
    */
  def isGlobaly: Boolean = scope.isEmpty
}

abstract class TaggedTypeInjectable[T] extends Injectable[T, ClassScope] {
  /**
    * Be accepted from request type.
    *
    * @param request Requesting type.
    * @return
    */
  def isAccepted(request: ClassScope[_]): Boolean = scope.exists { r =>
    r.isAssignableFrom(request)
  }
}

abstract class TaggedObjectInjectable[T] extends Injectable[T, ObjectScope] {
  /**
    * Be accepted from request type.
    *
    * @param request Requesting type.
    * @return
    */
  def isAccepted(request: ObjectScope[_]): Boolean = scope.contains(request)
}

trait InjectableConversion[ST[_]] {
  def toInjectable[T](tag: TypeTag[T], value: T, newScope: Seq[ST[_]]): Injectable[T, ST]
}