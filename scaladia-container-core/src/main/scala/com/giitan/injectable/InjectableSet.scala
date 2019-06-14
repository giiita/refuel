package com.giitan.injectable

import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

private[giitan] object InjectableSet {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  implicit class InjectableList[ST[_]](v: ListBuffer[Injectable[_, ST]]) {
    /**
      * Append dependencies object.
      * Overwrite if already registered.
      *
      * @param tag   Dependency object typed tag.
      * @param value Dependency object.
      * @param scope Acceptable scopes.
      * @tparam T Dependency object type.
      * @return
      */
    def overwrite[T](tag: TypeTag[T], value: T, scope: Seq[ST[_]])(implicit x: InjectableConversion[ST]): Unit = {
      v.searchAccessibles(tag.tpe, scope).map(x => v -= x)
      v += x.toInjectable(tag, value, scope)
    }

    /**
      * Search injectables with duplicate Scope.
      *
      * @param targetType Searched type.
      * @param accessFrom New injectable scopes.
      * @tparam T Dependency object type.
      * @return
      */
    def searchAccessibles[T](targetType: Type, accessFrom: Seq[ST[_]]): Seq[Injectable[_, ST]] =
      for {
        injectable <- v.toSeq
        if injectable.tipe =:= targetType
        if (accessFrom.isEmpty && injectable.isGlobaly) || (accessFrom.nonEmpty && accessFrom.exists(x => injectable.isAccepted(x)))
      } yield injectable

    /**
      * Search accessible T.
      * Prioritize injectable allowed by Scope.
      *
      * @param targetType Searched type.
      * @param accessFrom Where request from?
      * @tparam T Dependency object type.
      * @return
      */
    def searchAccessibleOne[T](targetType: Type, accessFrom: ST[_]): Option[T] = {
      {
        v.find(r => r.tipe =:= targetType && r.isAccepted(accessFrom)) orElse v.find(r => r.tipe =:= targetType && r.isGlobaly)
      }.map(_.applier.asInstanceOf[T])
    }
  }

}
