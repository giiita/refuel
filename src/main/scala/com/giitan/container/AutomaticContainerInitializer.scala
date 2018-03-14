package com.giitan.container

import com.giitan.box.Container
import org.slf4j.LoggerFactory

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] object AutomaticContainerInitializer {

  LoggerFactory.getLogger(this.getClass).debug(s"Loading from ${implicitly[Container].automaticDependencies.value.map(_.getSimpleName).mkString(", ")}")
  implicitly[Container].automaticDependencies.initialize()

  def initialize[T: ClassTag](tag: TypeTag[T]): Unit = {
    implicitly[Container].automaticDependencies.initialize(tag)
  }
}
