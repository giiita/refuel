package com.giitan

import com.giitan.box.{Container, ScaladiaClassLoader}
import com.giitan.exception.InjectableDefinitionException
import com.giitan.injectable.{Injectable, StoredDependency}
import com.giitan.injectable.InjectableSet._
import com.giitan.injector.Injector
import com.giitan.implicits._
import com.giitan.loader.RichClassCrowds.ClassCrowds
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
    val container: ListBuffer[Injectable[_]] = ListBuffer(
      toInjectly(
        typeTag[Indexer],
        FunctIndexer
      )
    )

    private[giitan] val automaticDependencies: ClassCrowds =
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
    private[giitan] def find[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S): StoredDependency[T] = {
      new StoredDependency[T] {
        protected val dependencyGet: () => T = () => {

          def search(tipe: Type): Option[T] = container.synchronized {
            (container.find(r => r.tipe =:= tipe && r.canAccess(scope.getClass)) or container.find(r => r.tipe =:= tipe && r.isGlobaly)).map(_.applier.asInstanceOf[T])
          }

          val tipe = tag.tpe

          search(tipe) match {
            case Some(x) => x
            case None =>
              AutomaticContainerInitializer.initialize[T]()
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
      * @tparam T
      */
    private[giitan] def indexing[T: TypeTag](tag: TypeTag[T], value: T): Unit = {
      container.synchronized {
        container.overwrite(tag, value)
      }
    }

    /**
      * Condense the accessible type.
      *
      * @param typTag Dependency object type.
      */
    private[giitan] def globaly(typTag: Type): Unit = {
      container.collect {
        case x if x.tipe =:= typTag => x
      }.foreach(r => {
        if (r.scope.isEmpty) container -= r
        else r.scope.clear()
      })
    }

    /**
      * Extend the accessible type.
      *
      * @param targetType Dependency object type.
      */
    private[giitan] def appendScope(typeTag: ScopeType, targetType: Type): Unit = {
      container.find(_.tipe =:= targetType) match {
        case Some(ij) => ij += typeTag
        case None => throw new InjectableDefinitionException(s"Uninjectable object. ${targetType.baseClasses.head}")
      }
    }
  }
}
