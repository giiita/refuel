package com.giitan.inject

import com.giitan.inject.AutoInjectorTest.AutoVariable
import com.giitan.injector.{AutoInjector, Injector}
import org.scalatest.{Assertion, Matchers, WordSpec}

object AutoInjectorTest {
  object AutoVariable extends AutoVariable {
    depends[AutoVariable](this)
  }

  trait AutoVariable extends AutoInjector {
  }
}

class AutoInjectorTest extends WordSpec with Matchers {
  "Auto injector test" should {
    "the simplest Injection" in {
      object Execute extends Injector {
        def test(): Assertion = {
          inject[AutoVariable].provide shouldBe AutoVariable
        }
      }

      Execute.test()
    }
  }
}
