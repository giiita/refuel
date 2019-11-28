package refuel.injector

import refuel.container.{Container, ContainerLifeCycle}
import refuel.internal.Macro

trait Injector extends MetaMediation[Container] {
  implicit var _cntMutation: Container = implicitly[ContainerLifeCycle].ctn
}