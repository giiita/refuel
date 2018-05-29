package com.giitan

import com.giitan.box.ScaladiaClassLoader.RichClassCrowd
import com.giitan.box.{Container, ScaladiaClassLoader}
import com.giitan.exception.InjectableDefinitionException
import com.giitan.injectable.{Injectable, StoredDependency}
import com.giitan.injectable.InjectableSet._
import com.giitan.injector.Injector
import com.giitan.implicits._
import com.giitan.scope.Scope.ScopeType
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

package object container {
  implicit val container: Container = new Container {

    val logger: Logger = LoggerFactory.getLogger(this.getClass)

    /* Injectable object mapper. */
    val v: ListBuffer[Injectable[_]] = ListBuffer.empty[Injectable[_]]

    private[giitan] val automaticDependencies: RichClassCrowd =
      ScaladiaClassLoader.findClasses()

    /**
      * Search accessible dependencies.
      *
      * @param tag   Dependency object typed tag.
      * @param scope Typed objects to be accessed.
      * @tparam T
      * @tparam S
      * @return
      */
    private[giitan] def find[T: ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S): StoredDependency[T] = {
      new StoredDependency[T] {
        protected val dependencyGet: () => T = () => {
          val callerType: ScopeType = scope.getClass

          def inScope(sc: Seq[ScopeType]): Boolean = sc.contains(callerType)

          def globalScope(sc: Seq[ScopeType]): Boolean = sc.isEmpty

          def search(tipe: Type): Option[T] = v.synchronized {
            (v.find(r => r.tipe =:= tipe && inScope(r.scope)) or v.find(r => r.tipe =:= tipe && globalScope(r.scope))).map(_.applier.asInstanceOf[T])
          }

          val tipe = tag.tpe

          search(tipe) match {
            case Some(x) => x
            case None =>
              AutomaticContainerInitializer.initialize(tag)
              search(tipe) >>> new InjectableDefinitionException(
                s"""$tipe or internal dependencies injected failed.""".stripMargin
              )
          }
        }
      }
    }

    /**
      * Regist dependencies.
      *
      * @param tag   Dependency object typed tag.
      * @param value Dependency object.
      * @param scope Typed objects to be accessed.
      * @tparam T
      */
    private[giitan] def indexing[T: TypeTag](tag: TypeTag[T], value: T, scope: ScopeType): Unit = {
      v.overwrite(tag, value, scope)
    }

    /**
      * Condense the accessible type.
      *
      * @param typTag Dependency object type.
      */
    private[giitan] def globaly(typTag: Type): Unit = {
      v.collect {
        case x if x.tipe =:= typTag => x
      }.foreach(r => {
        if (r.scope.isEmpty) v -= r
        else r.scope.clear()
      })
    }

    /**
      * Extend the accessible type.
      *
      * @param targetType Dependency object type.
      */
    private[giitan] def appendScope(typeTag: ScopeType, targetType: Type): Unit = {
      v.find(_.tipe =:= targetType) match {
        case Some(ij) => ij += typeTag
        case None => throw new InjectableDefinitionException(s"Uninjectable object. ${targetType.baseClasses.head}")
      }
    }
  }
}
