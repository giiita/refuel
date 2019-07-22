package com.phylage.scaladia.injector

import com.phylage.scaladia.container.{Container, ContainerStore}

trait Injector extends ContainerAccessible[Container] {
  implicit def _cntMutation: Container = implicitly[ContainerStore].ctn
}
