package com.phylage.scaladia.injector

import com.phylage.scaladia.provider.Lazy
import com.phylage.scaladia.provider.Tag
import com.phylage.scaladia.internal.Macro
import com.phylage.scaladia.container.indexer.Indexer
import com.phylage.scaladia.container.{Container, ContainerStore}
import com.phylage.scaladia.provider.Accessor

import scala.reflect.runtime.universe._
import scala.language.implicitConversions

object Injector {
  type @@[+T, +U] = T with Tag[U]
}

/**
  * It is a rule class to use Injection container.
  */
trait Injector {
  me =>

  /**
    * Manually register the new dependency.
    *
    * @param x new dependency.
    * @param priority Injection priority.
    * @tparam T new dependency type
    */
  protected def flush[T: WeakTypeTag](x: T, priority: Int = 1100): Unit = implicitly[Container].createIndexer(x, priority).indexing()

  /**
    * Gets an indexer for registering new dependencies.
    * By default, the dependency priority is set to maximum.
    *
    * @param x new dependency
    * @param priority Injection priority.
    * @tparam T new dependency type
    * @return
    */
  protected def narrow[T: WeakTypeTag](x: T, priority: Int = Int.MaxValue): Indexer[T] = implicitly[Container].createIndexer(x, priority)

  /**
    * Get accessible dependencies.
    *
    * @param ctn Container
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def inject[T](implicit ctn: Container, access: Accessor[_]): Lazy[T] = macro Macro.inject[T]

  /**
    * Provide dependency.
    *
    * @param variable Stored dependency object.
    * @tparam X Variable type
    * @return
    */
  implicit def provide[X](variable: Lazy[X]): X = variable.provide

  /**
    * This refers to itself
    * @return
    */
  implicit def from: Accessor[_] = Accessor(me)

  /**
    * Implicitly container
    * @return
    */
  implicit def getContainer: Container = implicitly[ContainerStore].ctn


}
