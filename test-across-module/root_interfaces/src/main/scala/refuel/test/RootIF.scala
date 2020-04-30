package refuel.test

import refuel.container.anno.RecognizedDynamicInjection
import refuel.injector.{AutoInject, Injector}

object RootIF extends Injector {

  trait Runner {
    def run: String = inject[Conf @RecognizedDynamicInjection].value
  }

  object Runner extends Runner with AutoInject

}

trait Conf {
  val value: String
}
