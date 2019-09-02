package com.phylage.scaladia.injector

import com.phylage.scaladia.container.{Container, ContainerLifeCycle}

trait Injector extends MetaMediation[Container] {
  implicit var _cntMutation: Container = implicitly[ContainerLifeCycle].ctn
}