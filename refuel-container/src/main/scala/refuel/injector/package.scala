package refuel

import refuel.container._

package object injector {

  private[refuel] implicit object ContainerLifeCycleImpl extends ContainerLifeCycle {
    lazy val ctn: Container = DefaultContainer()
    override val ijp: InjectionPool = RuntimeInjectionPool
  }

  import scala.language.implicitConversions

  implicit def _containerInheritance[T](x: HiddenContainerShade[T]): T = x.fx(x._cntMutation)
}
