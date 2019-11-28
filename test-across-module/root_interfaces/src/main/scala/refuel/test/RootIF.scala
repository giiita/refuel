package refuel.test

import refuel.injector.{AutoInject, Injector}

object RootIF extends Injector {

  trait Runner {
    def run: String = inject[Conf].value
  }

  object Runner extends Runner with AutoInject[Runner]

}

trait Conf {
  val value: String
}
