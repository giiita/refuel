package com.giitan.injectable

import com.giitan.scope.Scope.ScopeType
import org.slf4j.{Logger, LoggerFactory}
import com.giitan.implicits._

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
      * @param tag   Dependency object typed tag.
      * @param value Dependency object.
      * @param scope Acceptable scopes.
      * @tparam T
      * @return
      */
    def overwrite[T](tag: TypeTag[T], value: T, scope: Seq[ScopeType]): Unit = {
      v.searchAccessibles(tag.tpe, scope).map(x => v -= x)
      v += toInjectly(tag, value, scope)
    }

    /**
      * Search accessible T.
      * Prioritize injectable allowed by Scope.
      *
      * @param targetType Searched type.
      * @param accessFrom Where request from?
      * @tparam T
      * @return
      */
    def searchAccessibleOne[T](targetType: Type, accessFrom: ScopeType): Option[T] = {
      {
        v.find(r => r.tipe =:= targetType && r.isAccepted(accessFrom)) or v.find(r => r.tipe =:= targetType && r.isGlobaly)
      }.map(_.applier.asInstanceOf[T])
    }

    /**
      * Search injectables with duplicate Scope.
      *
      * @param targetType Searched type.
      * @param accessFrom New injectable scopes.
      * @tparam T
      * @return
      */
    def searchAccessibles[T](targetType: Type, accessFrom: Seq[ScopeType]): Seq[Injectable[_]] =
      for {
        injectable <- v
        if injectable.tipe =:= targetType
        if (accessFrom.isEmpty && injectable.isGlobaly) || (accessFrom.nonEmpty && accessFrom.exists(x => injectable.isAccepted(x)))
      } yield injectable
  }

  /**
    * Generate a injectly object.
    *
    * @param tag      Dependency object typed tag.
    * @param value    Dependency object.
    * @param newScope Acceptable scopes.
    * @tparam T
    * @return
    */
  def toInjectly[T](tag: TypeTag[T], value: T, newScope: Seq[ScopeType]): Injectable[T] = {
    new Injectable[T] {
      val tipe: Type = tag.tpe
      val applier: T = value
      val scope: Seq[ScopeType] = newScope
    }
  }
}
