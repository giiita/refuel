package refuel

import refuel.container._
import refuel.container.provider.Lazy
import refuel.inject.Types.LocalizedContainer

package object inject {

  private[refuel] implicit object ContainerLifeCycleImpl extends ContainerLifeCycle {
    lazy val container: LocalizedContainer = ContainerImpl()
  }

  import scala.language.implicitConversions

  implicit def _containerInheritance[T](x: HiddenContainerScope[T]): T = x.fx(x._cntRef)
}
