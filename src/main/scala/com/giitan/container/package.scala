package com.giitan

import com.giitan.box.ClassFinder.RichClassCrowd
import com.giitan.box.{ClassFinder, Container}
import com.giitan.injectable.Injectable
import com.giitan.injectable.InjectableSet._
import com.giitan.injector.Injector
import com.giitan.implicits._

import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

package object container {
  implicit val container: Container = new Container {

    /**
      * Injectable object mapper.
      */
    protected var v: Set[Injectable[_]] = Set.empty[Injectable[_]]

    private[giitan] val automaticDependencies: RichClassCrowd =
      ClassFinder().findClasses()

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
      def inScope(sc: Seq[Class[_]]): Boolean = sc.isEmpty || sc.contains(scope.getClass)

      def search(tipe: Type): Option[T] = {
        v.find(r => r.tipe == tipe && inScope(r.scope)) >>
          (_.applier.asInstanceOf[T])
      }

      val tipe = tag.tpe

      search(tipe) match {
        case Some(x) => x
        case None    =>
          println(s"★★★★★★★★★★★★★　Initialize $tag  【 ${v.map(_.tipe).mkString(", ")} 】")
          AutomaticContainerInitializer.initialize(tag)
          search(tipe) >>> new IllegalAccessException(s"Uninjectable object. ${tipe.baseClasses.head} 【 ${v.map(_.tipe).mkString(", ")} 】")
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
      v = v.overRide(tag, value, scope.getClass)
    }

    /**
      * Condense the accessible type.
      *
      * @param typTag Dependency object type.
      */
    private[giitan] def scoped(typTag: Type): Unit = {
      v.find(_.tipe == typTag) >> (_.clear)
    }

    /**
      * Extend the accessible type.
      *
      * @param typTag Dependency object type.
      */
    private[giitan] def scoped(clazz: Class[_], typTag: Type): Unit = {
      v.find(_.tipe == typTag) match {
        case Some(ij) => ij += clazz
        case None     => throw new IllegalAccessException(s"Uninjectable object. ${typTag.baseClasses.head}")
      }
    }
  }
}
