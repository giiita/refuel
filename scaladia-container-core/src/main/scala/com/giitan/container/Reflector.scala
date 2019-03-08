package com.giitan.container

import com.giitan.box.Container
import com.giitan.exception.InjectableDefinitionException
import com.giitan.injectable.StoredDependency
import com.giitan.injector.Injector
import com.giitan.scope.Scope.{ClassScope, ObjectScope}
import com.giitan.scope.Wrapper

import scala.reflect.runtime.universe._
import scala.reflect.ClassTag
import scala.util.{Failure, Success}

case class Reflector[T: TypeTag : ClassTag, S <: Injector : TypeTag](tag: TypeTag[T], scope: S, recoverHook: PartialFunction[Throwable, T] = PartialFunction[Throwable, T]{
  e => throw e
}) extends StoredDependency[T] {

  protected val dependencyGet: () => T = () => {
    implicitly[Container[ObjectScope]].search(tag, Wrapper(scope))
      .orElse(
        implicitly[Container[ClassScope]].search(tag, scope.getClass)
      ) getOrElse {
      throw new InjectableDefinitionException(
        s"""Cannot found ${tag.tpe} implementation.""".stripMargin
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
      scala.util.Try {
        val module = dependencyGet()
        lazyProvided = Some(module)
        module
      } match {
        case Success(x) => x
        case Failure(e) => recoverHook(e)
      }
    }
  }

  /**
    * Recovery hook when failure of dependency search fails.
    *
    * @param function Recovery hook.
    * @return
    */
  def recover(function: PartialFunction[Throwable, T]): StoredDependency[T] = {
    copy(
      recoverHook = function
    )
  }
}
