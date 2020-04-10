package refuel.injector

import refuel.container.{Container, ContainerLifeCycle}
import refuel.domination.InjectionPriority
import refuel.domination.InjectionPriority.Default

import scala.reflect.runtime.universe._

trait Injector extends MetaMediation[Container] {
  implicit var _cntMutation: Container = implicitly[ContainerLifeCycle].ctn

  protected final implicit class _Registrable[T](t: T) {
    def index[X >: T : WeakTypeTag](p: InjectionPriority = Default)(implicit c: Container): X = {
      c.createIndexer[X](t, p).indexing().value
    }
  }

}

object Injector {

}