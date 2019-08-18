package com.phylage.scaladia.test

import com.phylage.scaladia.test.Executor.{InjectionExecution, PureExecution}
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

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
