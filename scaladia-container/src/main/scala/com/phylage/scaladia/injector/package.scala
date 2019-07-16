package com.phylage.scaladia

import com.phylage.scaladia.Types.LocalizedContainer
import com.phylage.scaladia.container.{Container, ContainerStore, InjectionPool, StandardContainer}

package object injector {

  private[scaladia] implicit object IndirectContainerStore extends ContainerStore {
    lazy val ctn: Container = initContainer

    def initContainer: Container = StandardContainer()

    override val ijp: InjectionPool = InjectionPool
  }

  import scala.language.implicitConversions
  implicit def _containerInheritance[T](x: ImplicitContainerInheritation[T]): T = x.fx(x._cntMutation)

  class ImplicitContainerInheritation[T](val fx: LocalizedContainer => T) extends ContainerAccessible[LocalizedContainer] {
    it =>
    implicit var _cntMutation: LocalizedContainer = implicitly[ContainerStore].ctn.shading

    // override def shade[I](ctx: LocalizedContainer => I): I = throw new InjectDefinitionException("Recursive calls to shade() are prohibited.")
  }

}
