package com.phylage.scaladia

import com.phylage.scaladia.container.{Container, ContainerStore, StandardContainer, InjectionPool}

package object injector {

  private[scaladia] implicit object IndirectContainerStore extends ContainerStore {
    lazy val ctn: Container = initContainer

    def initContainer: Container = new StandardContainer()

    override val ijp: InjectionPool = InjectionPool
  }

}
