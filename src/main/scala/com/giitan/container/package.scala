package com.giitan

import com.giitan.box.Container
import com.giitan.exception.InjectableDefinitionException
import com.giitan.injectable.Injectable
import com.giitan.injectable.InjectableSet._
import com.giitan.injector.{AutoInjector, Injector}
import com.giitan.implicits._
import org.clapper.classutil.ClassFinder

import scala.language.implicitConversions
import scala.reflect.runtime.universe._

package object container {
  implicit val container: Container = new Container {

    /**
      * Injectable object mapper.
      */
    protected var v: Set[Injectable[_]] = Set.empty[Injectable[_]]

    private[this] val mirror = runtimeMirror(this.getClass.getClassLoader)

    private[giitan] val automaticDependencies: Iterator[ModuleMirror] =
      ClassFinder.concreteSubclasses(classOf[AutoInjector],  ClassFinder().getClasses()).map(r =>
        mirror.reflectModule(mirror.staticModule(r.name))
      )

    /**
      * Search accessible dependencies.
      *
      * @param tag Dependency object typed tag.
      * @param scope Typed objects to be accessed.
      * @tparam T
      * @tparam S
      * @return
      */
    private[giitan] def find[T, S <: Injector: TypeTag](tag: TypeTag[T], scope: S): T = {
      def inScope(sc: Seq[Class[_]]): Boolean = sc.isEmpty  || sc.contains(scope.getClass)

      automaticDependencies.foreach(_.instance)

      val tipe = tag.tpe

      v.find(r => r.tipe == tipe && inScope(r.scope)) >>
        (_.applier.asInstanceOf[T]) >>>
        new IllegalAccessException(s"Uninjectable object. ${tipe.baseClasses.head}")
    }

    /**
      * Regist dependencies.
      *
      * @param tag Dependency object typed tag.
      * @param value Dependency object.
      * @param scope Typed objects to be accessed.
      * @tparam T
      * @tparam S
      */
    private[giitan] def indexing[T: TypeTag, S <: Injector: TypeTag](tag: TypeTag[T], value: T, scope: S): Unit = {
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
        case None => throw new IllegalAccessException(s"Uninjectable object. ${typTag.baseClasses.head}")
      }
    }
  }
}
