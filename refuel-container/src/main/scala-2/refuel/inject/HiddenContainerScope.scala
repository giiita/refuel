package refuel.inject

import refuel.container.DependencyPoolRef
import refuel.inject.Types.LocalizedContainer

private[refuel] class HiddenContainerScope[T](val fx: LocalizedContainer => T)(c: LocalizedContainer)
    extends DependencyPoolRef[LocalizedContainer] {
  it =>
  implicit var _cntRef: LocalizedContainer = c

  def apply(): T = fx(c)
}
