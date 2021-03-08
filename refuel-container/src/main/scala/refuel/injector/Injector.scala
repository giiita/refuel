package refuel.injector

import refuel.container.{Container, ContainerLifeCycle}
import refuel.domination.InjectionPriority
import refuel.domination.InjectionPriority.{Default, Overwrite}

import scala.reflect.runtime.universe._

trait Injector extends MetaMediation[Container] {
  implicit var _cntRef: Container = implicitly[ContainerLifeCycle].ctn

  protected final implicit class _Registrable[T](t: T) {
    def index[X >: T: WeakTypeTag](p: InjectionPriority = Overwrite)(implicit c: Container): X = {
      c.createIndexer[X](t, p, Vector.empty).indexing().value
    }
  }
}
