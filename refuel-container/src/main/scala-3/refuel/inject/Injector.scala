package refuel.inject

import refuel.container.indexer.Indexer
import refuel.container.macros.Macro
import refuel.container.{Container, ContainerImpl, ContainerLifecycle, DependencyPoolRef, IndexedKey}
import refuel.container.provider.Accessor
import refuel.inject.Types.LocalizedContainer
import refuel.container.provider.{Accessor, Lazy}

import scala.quoted.Type
import scala.language.implicitConversions

trait Injector(using __rc: LocalizedContainer = summon[ContainerLifecycle].container) extends DependencyPoolRef[LocalizedContainer] { me =>
  /* Container instance */
  this.__refuel_cRef = Some(__rc)
  /**
   * This refers to itself.
   * When injecting, check if this Accessor is authorized.
   *
   * @return
   */
  protected given __refuel_accessor: Accessor[_] = Accessor(me)

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
   *     new BStub().index[B]()
   *     inject[D].run // Use BStub in C in D
   *   }
   *   inject[D].run // Use A in B in C in D
   * }}}
   *
   * @param ctx Shaded container function.
   * @tparam T Result type
   * @return
   */
  def extended[T](ctx: LocalizedContainer ?=> T)(using _c: LocalizedContainer): T =
    new HiddenContainerScope(ctx)(using _c.shading)

  def closed[T](ctx: LocalizedContainer ?=> T): T =
    new HiddenContainerScope(ctx)(using ContainerImpl())


  /**
   * Get accessible dependencies.
   *
   * The type information is resolved at compile time, but the injection object is finalized at runtime.
   *
   * @param access Accessor (This refers to itself)
   * @tparam T Injection type
   * @return
   */
  protected inline def inject[T]: refuel.container.provider.Lazy[T] = ${Macro.inject[T]}


  /**
   * Gets an indexer for registering new dependencies.
   * By default, the dependency priority is set to maximum.
   *
   * @param x        new dependency
   * @param priority Injection priority.
   * @tparam T new dependency type
   * @return
   */

  extension [T] (inline self: T) {
    protected inline def indexer[X >: T](inline priority: InjectionPriority = InjectionPriority.Overwrite): Indexer[X] = ${Macro.indexer[X]('priority, 'self)}
    protected inline def index[X >: T](inline p: InjectionPriority = InjectionPriority.Overwrite): X = ${Macro.index[X]('p, 'self)}
  }
}

object Injector {
  given __refuel_Registrable[T]: scala.Conversion[HiddenContainerScope[T], T] = { x => x() }
}
