package com.phylage.scaladia.container

import com.phylage.scaladia.injector.InjectionPool

private[scaladia] trait ContainerStore {
  val ctn: Container
  val ijp: InjectionPool
}
