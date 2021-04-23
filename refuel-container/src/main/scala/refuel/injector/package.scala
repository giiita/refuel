package refuel

import refuel.container._
import refuel.provider.Lazy

package object injector {

  private[refuel] implicit object ContainerLifeCycleImpl extends ContainerLifeCycle {
    lazy val ctn: Container         = DefaultContainer()
    override val ijp: InjectionPool = RuntimeInjectionPool
  }

  import scala.language.implicitConversions

  implicit def _containerInheritance[T](x: HiddenContainerShade[T]): T = x.fx(x._cntRef)

  implicit def _explicitProviding[X](implicit variable: Lazy[X], ctn: Container): X = {
    variable._provide
  }
}
