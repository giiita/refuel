package com.giitan.injectable

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

object InjectableSet {
  implicit class InjectableList(v: ListBuffer[Injectable[_]]) {
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
    def overwrite[T: TypeTag](tag: TypeTag[T], value: T, scope: Class[_]): Unit = {
      def inScope(sc: Seq[Class[_]]): Boolean = sc.isEmpty || sc.contains(scope)

      val tipe = typeOf[T]

      v.find(r => {
        r.tipe == tipe && inScope(r.scope)
      }) match {
        case Some(x) => v -= x
        case _ =>
      }
      v += toInjectly(tag, value, scope)
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

      def clear: Unit = this.scope.clear()

      def +=(c: Class[_]): Injectable[T] = {
        scope += c
        this
      }
    }
  }
}
