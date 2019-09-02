package com.phylage.scaladia.injector

import com.phylage.scaladia.Types.LocalizedContainer

private[scaladia] class ImplicitContainerInheritation[T](val fx: LocalizedContainer => T)(c: LocalizedContainer) extends MetaMediation[LocalizedContainer] {
  it =>
  implicit var _cntMutation: LocalizedContainer = c
}


