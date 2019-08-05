package com.phylage.scaladia

import com.phylage.scaladia.Types.LocalizedContainer
import com.phylage.scaladia.container.{Container, ContainerStore, MacroInjectionPool, StandardContainer}

package object injector {

  private[scaladia] implicit object IndirectContainerStore extends ContainerStore {
    lazy val ctn: Container = initContainer
    override val ijp: InjectionPool = MacroInjectionPool

    def initContainer: Container = new StandardContainer()
  }

  import scala.language.implicitConversions

  implicit def _containerInheritance[T](x: ImplicitContainerInheritation[T]): T = x.fx(x._cntMutation)

  class ImplicitContainerInheritation[T](val fx: LocalizedContainer => T)(c: LocalizedContainer) extends ContainerAccessible[LocalizedContainer] {
    it =>
    implicit var _cntMutation: LocalizedContainer = c
  }

}
