package com.giitan.loader

import com.giitan.runtime.ScaladiaClassLoader.classLoader
import com.giitan.exception.StaticInitializationException
import com.giitan.injector.AutoInject

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

import scala.reflect._
import runtime.universe._

object RichClassCrowds {
  object ClassCrowds {
    def apply(request: Seq[ClassCrowd[_]]): ClassCrowds = new ClassCrowds(
      ListBuffer.from(request)
    )
  }

  implicit class ClassCrowds(val value: ListBuffer[ClassCrowd[_]] = ListBuffer.empty) {

    private[this] def fire[T: TypeTag](crowd: ClassCrowd[T]): Unit = {
      drop(crowd)

      val classDef = crowd.instanceClass

      val mirror = runtimeMirror(classLoader)
      if (classDef.getName.trim.endsWith("$")) {
        try {
          mirror.reflectModule(mirror.staticModule(classDef.getName)).instance.asInstanceOf[AutoInject[T]].registForContainer[T]
        } catch {
          case e: Throwable => throw new StaticInitializationException(s"${classDef.getSimpleName} initialize failed.", e)
        }
      }
    }

    def asType[X: TypeTag](x: Class[X]): Type = typeOf[X]

    def initialize[T: TypeTag : ClassTag](): Unit = {
      value.collect {
        case x if typeOf[AutoInject[T]] =:= x.dependencyType => fire(x.asInstanceOf[ClassCrowd[T]])
      }
    }

    def +++(next: ClassCrowds): ClassCrowds = ClassCrowds(value ++= next.value)

    def drop[T](dropClass: ClassCrowd[T]): Unit = value -= dropClass
  }
}
