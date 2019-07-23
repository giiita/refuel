package com.phylage.scaladia.test

import com.phylage.scaladia.injector.RefreshInjection
import com.phylage.scaladia.test.Executor.{InjectionExecution, PureExecution}
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

import scala.util.{Failure, Success, Try}

class ModuleAcrossInjectionTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with RefreshInjection {
  "Module across injection test" should {
    "pure call" in {
      Try {
        PureExecution.exec shouldBe ConfImpl.value
      } match {
        case Success(_) => fail("It can not be solved by the internal call which does not pass injection")
        case Failure(e) => e.getMessage shouldBe "interface com.phylage.scaladia.test.Conf or its internal initialize failed."
      }
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
