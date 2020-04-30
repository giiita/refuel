package refuel.injector

import refuel.Types.LocalizedContainer
import refuel.container._
import refuel.container.indexer.Indexer
import refuel.domination.InjectionPriority
import refuel.domination.InjectionPriority.{Overwrite, Primary}
import refuel.internal.Macro
import refuel.provider.{Accessor, Lazy}

import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.runtime.universe._

/**
  * It is a rule class to use Injection container.
  */
private[refuel] trait MetaMediation[C <: Container] extends CanBeContainer[C] {
  me =>

  /**
    * Implicitly injection pool
    *
    * @return
    */
  protected implicit def _ijp: InjectionPool =
    implicitly[ContainerLifeCycle].ijp

  /**
    * Manually register the new dependency.
    *
    * @param x        new dependency.
    * @param priority Injection priority.
    * @tparam T new dependency type
    */
  @deprecated("Try (t).index() instead.")
  def overwrite[T: WeakTypeTag](x: T, priority: InjectionPriority = Overwrite)(
      implicit ctn: C
  ): Unit = ctn.createIndexer(x, priority, Vector.empty).indexing()

  /**
    * Create a container shade.
    * You can't nest shading.
    *
    * {{{
    *   class A(value: String)
    *   class B(a: A)
    *   class C(b: B)
    *   class D(c: C)
    *   shade { implicit c =>
    *     new BStub().index()
    *     inject[D].run // Use BStub in C in D
    *   }
    *   inject[D].run // Use A in B in C in D
    * }}}
    *
    * @param ctx Shaded container function.
    * @tparam T Result type
    * @return
    */
  def shade[T](ctx: LocalizedContainer => T): T =
    new HiddenContainerShade(ctx)(_cntMutation.shading)

  /**
    * Gets an indexer for registering new dependencies.
    * By default, the dependency priority is set to maximum.
    *
    * @param x        new dependency
    * @param priority Injection priority.
    * @tparam T new dependency type
    * @return
    */
  protected def narrow[T: WeakTypeTag](
      x: T,
      priority: InjectionPriority = Primary
  )(implicit ctn: C): Indexer[T] = ctn.createIndexer(x, priority)

  /**
    * Get accessible dependencies.
    *
    * The type information is resolved at compile time, but the injection object is finalized at runtime.
    *
    * @param ctn    Container
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def inject[T](implicit ctn: C, ip: InjectionPool, access: Accessor[_]): Lazy[T] =
    macro Macro.lazyInject[T]
}
