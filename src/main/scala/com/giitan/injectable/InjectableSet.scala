package com.giitan.injectable

import scala.language.implicitConversions
import scala.reflect.runtime.universe._

object InjectableSet {
  implicit class InjectableList(v: Set[Injectable[_]]) {
    /**
      * Append dependencies object.
      * Overwrite if already registered.
      *
      * @param tag Dependency object typed tag.
      * @param value Dependency object.
      * @param scope Typed objects to be accessed.
      * @tparam T
      * @return
      */
    def overRide[T: TypeTag](tag: TypeTag[T], value: T, scope: Class[_]): Set[Injectable[_]] = {
      def inScope(sc: Seq[Class[_]]): Boolean = sc.isEmpty || sc.contains(scope)

      val tipe = typeOf[T]
      v.filter(r =>
        r.tipe != tipe || (r.tipe == tipe && !inScope(r.scope))
      ) + toInjectly(tag, value, scope)
    }
  }

  /**
    * Generate a injectly object.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @param stype Typed objects to be accessed.
    * @tparam T
    * @return
    */
  def toInjectly[T: TypeTag](tag: TypeTag[T], value: T, stype: Class[_]): Injectable[T] = {
    new Injectable[T] {
      val tipe: Type = tag.tpe
      val applier: T = value
      var scope: Seq[Class[_]] = Seq(stype)

      def clear: Unit = this.scope = Nil

      def +=(c: Class[_]): Injectable[T] = {
        this.scope = scope :+ c
        this
      }
    }
  }
}
