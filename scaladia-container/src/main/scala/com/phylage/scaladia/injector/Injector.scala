package com.phylage.scaladia.injector

import com.phylage.scaladia.container.{Container, ContainerStore}

trait Injector extends MetaMediation[Container] {
  implicit var _cntMutation: Container = implicitly[ContainerStore].ctn
}