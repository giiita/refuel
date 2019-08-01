package com.phylage.scaladia.test

import com.phylage.scaladia.injector.RefreshInjection
import com.phylage.scaladia.test.Executor.{InjectionExecution, PureExecution}
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

class ModuleAcrossInjectionTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with RefreshInjection {
  "Module across injection test" should {
    "pure call" in {
      PureExecution.exec shouldBe ConfImpl.value
    }

    "pure call with reify" in {
      reify {
        PureExecution.exec shouldBe ConfImpl.value
      }
    }

    "call after injection" in {
      InjectionExecution.exec shouldBe ConfImpl.value
    }
  }
}
