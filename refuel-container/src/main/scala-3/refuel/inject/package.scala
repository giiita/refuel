package refuel.inject

import refuel.container.{Container, ContainerImpl, ContainerLifecycle}
import refuel.container.provider.Lazy
import refuel.inject.Types.LocalizedContainer

import scala.language.implicitConversions

given _containerInheritance[T]: scala.Conversion[HiddenContainerScope[T], T] = { x => x.fx(using x.__refuel_c) }

private[refuel] given ContainerLifecycleImpl: ContainerLifecycle with {
  private[this] lazy val _container: LocalizedContainer = ContainerImpl()

  def container: LocalizedContainer = _container
}

/**
 * Provide internal instance Lazy[T].
 * Once injected, the object is persisted.
 * However, if a request from a different container instance occurs, it may be searched again.
 *
 * @param variable Stored dependency object.
 * @tparam X Variable type
 * @return
 */
given _implicitProviding[X]: scala.Conversion[Lazy[X], X] = {
  _._provide
}

given _implicitNestedProviding[X]: scala.Conversion[Lazy[Lazy[X]], X] = {
  _._provide._provide
}
