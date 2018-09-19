package com.giitan.container

import com.giitan.box.Container
import com.giitan.exception.InjectableDefinitionException
import com.giitan.injectable.StoredDependency
import com.giitan.injector.Injector
import com.giitan.scope.Scope.{ClassScope, ObjectScope}
import com.giitan.scope.Wrapper
import scala.reflect.runtime.universe._

import scala.reflect.ClassTag

class Reflector[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S) extends StoredDependency[T] {

  protected val dependencyGet: () => T = () => {
    implicitly[Container[ObjectScope]].search(tag, Wrapper(scope))
      .orElse(
        implicitly[Container[ClassScope]].search(tag, scope.getClass)
      ) getOrElse {
      throw new InjectableDefinitionException(
        s"""${tag.tpe} or internal dependencies injected failed.""".stripMargin
      )
    }
  }

  /**
    * Provide dependency and make it persistent.
    *
    * @return
    */
  def provide: T = lazyProvided.synchronized {
    lazyProvided getOrElse {
      val module = dependencyGet()
      lazyProvided = Some(module)
      module
    }
  }
}
