package com.giitan.inject

import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}


class AutoInjectorTest extends WordSpec with Matchers {
  "inject" should {
    "the simplest Injection" in {
      object Execute extends Injector {
        def test(): Assertion = {
          inject[TopLevelAutoInjectable] shouldBe TopLevelAutoInjectable
        }
      }

      Execute.test()
    }
    "Injection not setting" in {
      object Execute extends Injector {
        def test(): Assertion = {
          inject[UnInjectable] shouldBe UnInjectable
        }
      }

      Execute.test()
    }
  }
}
