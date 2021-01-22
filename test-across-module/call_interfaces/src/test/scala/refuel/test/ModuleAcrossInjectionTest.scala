package refuel.test

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.test.Executor.{InjectionExecution, PureExecution}

class ModuleAcrossInjectionTest extends AsyncWordSpec with Matchers with Diagrams {
  "Module across injection test" should {
    "pure call" in {
      PureExecution.exec shouldBe ConfImpl.value
    }

    "call after injection" in {
      InjectionExecution.exec shouldBe ConfImpl.value
    }
  }
}
