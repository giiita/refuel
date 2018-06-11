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
        FunctIndexer,
        Nil
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
          val tipe = tag.tpe

          container.searchAccessibleOne[T](tipe, scope.getClass) match {
            case Some(x) => x
            case None =>
              AutomaticContainerInitializer.initialize[T]()
              container.searchAccessibleOne[T](tipe, scope.getClass) >>> new InjectableDefinitionException(
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
      * @param scope Acceptable scopes.
      * @tparam T
      */
    private[giitan] def indexing[T](tag: TypeTag[T], value: T, scope: Seq[ScopeType]): Unit = {
      container.synchronized {
        container.overwrite(tag, value, scope)
      }
    }
  }
}
