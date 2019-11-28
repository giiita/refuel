package refuel.test

import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}
import refuel.test.Executor.{InjectionExecution, PureExecution}

class ModuleAcrossInjectionTest extends AsyncWordSpec with Matchers with DiagrammedAssertions {
  "Module across injection test" should {
    "pure call" in {
      PureExecution.exec shouldBe ConfImpl.value
    }

    "call after injection" in {
      InjectionExecution.exec shouldBe ConfImpl.value
    }
  }
}
