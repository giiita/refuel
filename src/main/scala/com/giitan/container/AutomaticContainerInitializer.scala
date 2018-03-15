package com.giitan.container

import com.giitan.box.Container
import org.slf4j.LoggerFactory

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] object AutomaticContainerInitializer {

  // (this.getClass).debug(s"Loading from ${implicitly[Container].automaticDependencies.value.map(_.getSimpleName).mkString(", ")}")
  println(s"Loading from ${implicitly[Container].automaticDependencies.value.map(_.getSimpleName).mkString("\n ")}")
  implicitly[Container].automaticDependencies.initialize()
  println(s"Load result ${implicitly[Container].v.map(_.tipe).mkString("\n ")}")

  def initialize[T: ClassTag](tag: TypeTag[T]): Unit = {
    implicitly[Container].automaticDependencies.initialize(tag)
  }
}
