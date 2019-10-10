package com.phylage.scaladia

import com.phylage.scaladia.container._

package object injector {

  private[scaladia] implicit object ContainerLifeCycleImpl extends ContainerLifeCycle {
    lazy val ctn: Container = DefaultContainer()
    override val ijp: InjectionPool = RuntimeInjectionPool
  }

  import scala.language.implicitConversions

  implicit def _containerInheritance[T](x: HiddenContainerShade[T]): T = x.fx(x._cntMutation)
}
