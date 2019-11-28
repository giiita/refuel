package refuel.test

import refuel.injector.Injector
import refuel.test.RootIF.Runner

object Executor extends Injector {

  object PureExecution {
    def exec: String = Runner.run
  }

  object InjectionExecution extends Injector {
    def exec: String = inject[Runner].run
  }

}
