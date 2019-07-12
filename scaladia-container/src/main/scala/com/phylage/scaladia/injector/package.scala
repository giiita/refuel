package com.phylage.scaladia

import com.phylage.scaladia.container.{Container, ContainerStore, InjectionPool, StandardContainer}
import com.phylage.scaladia.injector.Injector.ImplicitContainerInheritation

package object injector {

  private[scaladia] implicit object IndirectContainerStore extends ContainerStore {
    lazy val ctn: Container = initContainer

    def initContainer: Container = StandardContainer()

    override val ijp: InjectionPool = InjectionPool
  }

  implicit def _containerInheritance[T](x: ImplicitContainerInheritation[T]): T = x.fx
}
