package com.giitan.injectable

import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

object InjectableSet {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  implicit class InjectableList(v: ListBuffer[Injectable[_]]) {
    /**
      * Append dependencies object.
      * Overwrite if already registered.
      *
      * @param tag Dependency object typed tag.
      * @param value Dependency object.
      * @tparam T
      * @return
      */
    def overwrite[T: TypeTag](tag: TypeTag[T], value: T): Unit = {
      val tipe = typeOf[T]

      v.filter(r => {
        r.tipe =:= tipe && r.isGlobaly
      }).map(x => v -= x)
      v += toInjectly(tag, value)
    }
  }

  /**
    * Generate a injectly object.
    *
    * @param tag Dependency object typed tag.
    * @param value Dependency object.
    * @tparam T
    * @return
    */
  def toInjectly[T: TypeTag](tag: TypeTag[T], value: T): Injectable[T] = {
    new Injectable[T] {
      val tipe: Type = tag.tpe
      val applier: T = value
    }
  }
}
