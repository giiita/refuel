package com.giitan.scope

import com.giitan.injectable.{Injectable, InjectableConversion, TaggedObjectInjectable, TaggedTypeInjectable}
import com.giitan.scope.Scope.{ClassScope, ObjectScope}

import scala.reflect.runtime.universe._

object TaggingClassConversions {
  implicit val classTagConversion: InjectableConversion[Class] = new InjectableConversion[Class] {
    def toInjectable[T](tag: TypeTag[T], value: T, newScope: Seq[Class[_]]): Injectable[T, Class] = new TaggedTypeInjectable[T] {
      val tipe: Type = tag.tpe
      val applier: T = value
      val scope: Seq[ClassScope[_]] = newScope
    }
  }

  implicit val objectTagConversion: InjectableConversion[ObjectScope] = new InjectableConversion[ObjectScope] {
    def toInjectable[T](tag: TypeTag[T], value: T, newScope: Seq[ObjectScope[_]]): Injectable[T, ObjectScope] = new TaggedObjectInjectable[T] {
      val tipe: Type = tag.tpe
      val applier: T = value
      val scope: Seq[ObjectScope[_]] = newScope
    }
  }
}