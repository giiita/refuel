package com.giitan

import com.giitan.box.ScaladiaClassLoader.RichClassCrowd
import com.giitan.box.{Container, ScaladiaClassLoader}
import com.giitan.exception.InjectableDefinitionException
import com.giitan.injectable.Injectable
import com.giitan.injectable.InjectableSet._
import com.giitan.injector.Injector
import com.giitan.implicits._

import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

package object container {
  implicit val container: Container = new Container {

    /**
      * Injectable object mapper.
      */
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
    private[giitan] def find[T: ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S): T = {
      def inScope(sc: Seq[Class[_]]): Boolean = sc.contains(scope.getClass)
      def globalScope(sc: Seq[Class[_]]): Boolean = sc.isEmpty

      def search(tipe: Type): Option[T] =
        (v.find(r => r.tipe == tipe && inScope(r.scope)) or v.find(r => r.tipe == tipe && globalScope(r.scope))) >> (_.applier.asInstanceOf[T])

      val tipe = tag.tpe

      search(tipe) match {
        case Some(x) => x
        case None    =>

          AutomaticContainerInitializer.initialize(tag)
          search(tipe) >>> new InjectableDefinitionException(
            s"""$tipe or internal dependencies injected failed.
               |Injectable sets:
               |  ${v.map(r => s"${r.applier.getClass.getSimpleName} as ${r.tipe}").mkString("\n  ")}
               |""".stripMargin
          )
      }
    }

    /**
      * Regist dependencies.
      *
      * @param tag   Dependency object typed tag.
      * @param value Dependency object.
      * @param scope Typed objects to be accessed.
      * @tparam T
      * @tparam S
      */
    private[giitan] def indexing[T: TypeTag, S <: Injector : TypeTag](tag: TypeTag[T], value: T, scope: S): Unit = {
      v.overwrite(tag, value, scope.getClass)
    }

    /**
      * Condense the accessible type.
      *
      * @param typTag Dependency object type.
      */
    private[giitan] def scoped(typTag: Type): Unit = {
      v.collect {
        case x if x.tipe == typTag => x
      }.foreach(r => {
        if (r.scope.isEmpty) v -= r
        else r.scope.clear()
      })
    }

    /**
      * Extend the accessible type.
      *
      * @param typTag Dependency object type.
      */
    private[giitan] def scoped(clazz: Class[_], typTag: Type): Unit = {
      v.find(_.tipe == typTag) match {
        case Some(ij) => ij += clazz
        case None     => throw new InjectableDefinitionException(s"Uninjectable object. ${typTag.baseClasses.head}")
      }
    }
  }
}
