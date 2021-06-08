package refuel.inject

import refuel.container.`macro`.Macro
import refuel.container.indexer.Indexer
import refuel.container.{Container, ContainerImpl, ContainerLifeCycle, DependencyPoolRef, IndexedKey}
import refuel.container.provider.{Accessor, Lazy}
import refuel.inject.InjectionPriority.Overwrite
import refuel.inject.Types.LocalizedContainer

import scala.language.implicitConversions
import scala.language.experimental.macros

trait Injector extends DependencyPoolRef[LocalizedContainer] { me =>
  /* Container instance */
  this.__refuel_cRef = Some(implicitly[ContainerLifeCycle].container)

  /**
    * This refers to itself.
    * When injecting, check if this Accessor is authorized.
    *
    * @return
    */
  protected implicit val __refuel_accessor: Accessor[_] = Accessor(me)

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
  def extended[T](ctx: LocalizedContainer => T)(implicit c: Container): T =
    new HiddenContainerScope(ctx)(c.shading)

  def closed[T](ctx: LocalizedContainer => T): T =
    new HiddenContainerScope(ctx)(ContainerImpl())

  /**
    * Get accessible dependencies.
    *
    * The type information is resolved at compile time, but the injection object is finalized at runtime.
    *
    * @param access Accessor (This refers to itself)
    * @tparam T Injection type
    * @return
    */
  protected def inject[T]: refuel.container.provider.Lazy[T] = macro Macro.inject[T]

  /**
    * Provide internal instance Lazy[T].
    * Once injected, the object is persisted.
    * However, if a request from a different container instance occurs, it may be searched again.
    *
    * @param variable Stored dependency object.
    * @tparam X Variable type
    * @return
    */
  implicit def _implicitProviding[X](variable: Lazy[X])(implicit ctn: LocalizedContainer): X = {
    variable._provide(ctn)
  }

  implicit def _implicitNestedProviding[X](variable: Lazy[Lazy[X]])(implicit ctn: LocalizedContainer): X = {
    variable._provide(ctn)._provide(ctn)
  }

  /**
    * Gets an indexer for registering new dependencies.
    * By default, the dependency priority is set to maximum.
    *
    * @param x        new dependency
    * @param priority Injection priority.
    * @tparam T new dependency type
    * @return
    */
  protected final implicit class __refuel_X[T](_self: T) {
    import scala.reflect.runtime.universe._
    def indexer[X >: T: WeakTypeTag](p: InjectionPriority = Overwrite)(implicit c: Container): Indexer[X] = {
      c.createIndexer[X](IndexedKey.runtime(weakTypeTag[X].tpe), _self, p, Vector.empty)
    }
    def index[X >: T: WeakTypeTag](p: InjectionPriority = Overwrite)(implicit c: Container): X = {
      indexer[X](p)(weakTypeTag[X], c).indexing().value
    }
  }
}

object Injector {
  implicit def __refuel_Registrable[T](_r: HiddenContainerScope[T]): T = { _r.apply() }
}
