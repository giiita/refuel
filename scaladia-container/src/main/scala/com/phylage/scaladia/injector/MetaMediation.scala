package com.phylage.scaladia.injector

import com.phylage.scaladia.Types.LocalizedContainer
import com.phylage.scaladia.container._
import com.phylage.scaladia.container.indexer.Indexer
import com.phylage.scaladia.internal.Macro
import com.phylage.scaladia.provider.{Accessor, Lazy}

import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

/**
  * It is a rule class to use Injection container.
  */
private[scaladia] trait MetaMediation[C <: Container] extends CanBeContainer[C] { me =>

  /**
    * Implicitly injection pool
    *
    * @return
    */
  protected implicit def _ijp: InjectionPool = implicitly[ContainerLifeCycle].ijp

  /**
    * Manually register the new dependency.
    *
    * @param x        new dependency.
    * @param priority Injection priority.
    * @tparam T new dependency type
    */
  def overwrite[T: WeakTypeTag](x: T, priority: Int = AutoInject.DEFAULT_INJECTION_PRIORITY + 100)(implicit ctn: C): Unit = ctn.createIndexer(x, priority, Vector.empty).indexing()

  /**
    * Create a container shade.
    *
    * @param ctx Shaded container function.
    * @tparam T Result type
    * @return
    */
  def shade[T](ctx: LocalizedContainer => T): T = new HiddenContainerShade(ctx)(_cntMutation.shading)

  /**
    * Gets an indexer for registering new dependencies.
    * By default, the dependency priority is set to maximum.
    *
    * @param x        new dependency
    * @param priority Injection priority.
    * @tparam T new dependency type
    * @return
    */
  protected def narrow[T: WeakTypeTag](x: T, priority: Int = Int.MaxValue)(implicit ctn: C): Indexer[T] = ctn.createIndexer(x, priority)

  /**
    * Get accessible dependencies.
    *
    * The type information is resolved at compile time, but the injection object is finalized at runtime.
    * This function is slower than [[com.phylage.scaladia.injector.MetaMediation.bind]], but can be overwritten by flush or narrow.
    *
    * @param ctn    Container
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def inject[T](implicit ctn: C, ip: InjectionPool, access: Accessor[_]): Lazy[T] = macro Macro.lazyInject[T]

  /**
    * Get accessible dependencies.
    * You can detect errors that can not be assigned at compile time.
    *
    * It is faster than [[com.phylage.scaladia.injector.MetaMediation.inject]] because of immediate assignment,
    * but the dependency injected at compile time is determined,
    * and this assignment can not be overwritten with "flush" or "narrow".
    *
    * The scope to which this immediate assignment applies is
    * all the same instances that inherit [[com.phylage.scaladia.injector.AutoInjectable]].
    *
    * @param ctn    Container
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def bind[T](implicit ctn: C, ip: InjectionPool, access: Accessor[_]): T = macro Macro.diligentInject[T]

}
