package com.phylage.scaladia.test

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.test.RootIF.Runner

object Executor extends Injector {

  object PureExecution {
    def exec: String = Runner.run
  }

  object InjectionExecution extends Injector {
    def exec: String = inject[Runner].run
  }

}
