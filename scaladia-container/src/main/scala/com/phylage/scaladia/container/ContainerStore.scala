package com.phylage.scaladia.container

private[scaladia] trait ContainerStore {
  val ctn: Container
  val ijp: com.phylage.scaladia.injector.InjectionPool
}
