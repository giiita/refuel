package com.giitan.container

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] object AutomaticContainerInitializer {
  def initialize[T: TypeTag : ClassTag](): Unit = {
    TaggedContainer.automaticDependencies.initialize[T]()
  }
}
