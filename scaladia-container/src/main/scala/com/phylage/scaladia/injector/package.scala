package com.phylage.scaladia

import com.phylage.scaladia.container._

package object injector {

  private[scaladia] implicit object ContainerLifeCycleImpl extends ContainerLifeCycle {
    lazy val ctn: Container = new StandardContainer()
    override val ijp: InjectionPool = RuntimeInjectionPool
  }

  import scala.language.implicitConversions

  implicit def _containerInheritance[T](x: ImplicitContainerInheritation[T]): T = x.fx(x._cntMutation)
}
