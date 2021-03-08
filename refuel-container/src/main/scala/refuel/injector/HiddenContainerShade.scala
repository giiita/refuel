package refuel.injector

import refuel.Types.LocalizedContainer

private[refuel] class HiddenContainerShade[T](val fx: LocalizedContainer => T)(c: LocalizedContainer)
    extends MetaMediation[LocalizedContainer] {
  it =>
  implicit var _cntRef: LocalizedContainer = c
}
