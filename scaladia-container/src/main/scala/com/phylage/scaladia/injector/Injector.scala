package com.phylage.scaladia.injector

import com.phylage.scaladia.container.{Container, ContainerLifeCycle}
import com.phylage.scaladia.internal.Macro

trait Injector extends MetaMediation[Container] {
  implicit var _cntMutation: Container = implicitly[ContainerLifeCycle].ctn
}