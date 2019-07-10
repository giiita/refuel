package com.phylage.scaladia.injector

import com.phylage.scaladia.provider.Lazy
import com.phylage.scaladia.provider.Tag
import com.phylage.scaladia.internal.Macro
import com.phylage.scaladia.container.indexer.Indexer
import com.phylage.scaladia.container.{Container, ContainerStore}
import com.phylage.scaladia.provider.Accessor

import scala.reflect.runtime.universe._
import scala.language.experimental.macros
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
  def overwrite[T: WeakTypeTag](x: T, priority: Int = 1100): Unit = implicitly[Container].createIndexer(x, priority).indexing()

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
    * The type information is resolved at compile time, but the injection object is finalized at runtime.
    * This function is slower than [[com.phylage.scaladia.injector.Injector.confirm]], but can be overwritten by flush or narrow.
    *
    * @param ctn Container
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def inject[T](implicit ctn: Container, ip: InjectionPool, access: Accessor[_]): Lazy[T] = macro Macro.lazyInject[T]


  /**
    * Get accessible dependencies.
    * You can detect errors that can not be assigned at compile time.
    *
    * It is faster than [[com.phylage.scaladia.injector.Injector.inject]] because of immediate assignment,
    * but the dependency injected at compile time is determined,
    * and this assignment can not be overwritten with "flush" or "narrow".
    *
    * The scope to which this immediate assignment applies is
    * all the same instances that inherit [[com.phylage.scaladia.injector.AutoInjectable]].
    *
    * @param ctn Container
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def confirm[T](implicit ctn: Container, ip: InjectionPool, access: Accessor[_]): T = macro Macro.diligentInject[T]

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
  protected implicit def someoneNeeds: Accessor[_] = Accessor(me)

  /**
    * Implicitly container
    * @return
    */
  protected implicit def _dicnt: Container = implicitly[ContainerStore].ctn

  /**
    * Implicitly injection pool
    * @return
    */
  protected implicit def _ijp: InjectionPool = implicitly[ContainerStore].ijp

}
