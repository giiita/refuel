package com.giitan.container

import com.giitan.box.Container
import com.giitan.scope.Scope.ClassScope

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

private[giitan] object AutomaticContainerInitializer {
  def initialize[T: TypeTag : ClassTag](): Unit = {
    implicitly[Container[ClassScope]].automaticDependencies.initialize[T]()
  }
}
