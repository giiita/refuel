package com.phylage.scaladia.injector

import com.phylage.scaladia.Types.LocalizedContainer

class ImplicitContainerInheritation[T](val fx: LocalizedContainer => T)(c: LocalizedContainer) extends ContainerAccessible[LocalizedContainer] {
  it =>
  implicit var _cntMutation: LocalizedContainer = c
}