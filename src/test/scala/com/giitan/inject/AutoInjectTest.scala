package com.giitan.inject

import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}

class AutoInjectTest extends WordSpec with Matchers {
  "Auto inject test" should {
    "the simplest Injection" in {
      object Execute extends Injector {
        def test(): Assertion = {
          inject[AutoVariable2] shouldBe AutoVariable2
        }
      }

      Execute.test()
    }
  }
}