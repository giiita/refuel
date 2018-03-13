package com.giitan.container

import com.giitan.box.Container

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] object AutomaticContainerInitializer {
  implicitly[Container].automaticDependencies.initialize()

  def initialize[T: ClassTag](tag: TypeTag[T]): Unit = {
    implicitly[Container].automaticDependencies.initialize(tag)
  }
}
