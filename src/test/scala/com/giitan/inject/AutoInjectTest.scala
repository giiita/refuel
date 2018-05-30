package com.giitan.inject

import com.giitan.inject.AutoInjectTest._
import com.giitan.injector.{AutoInject, Injector}
import org.scalatest.{Assertion, Matchers, WordSpec}

object AutoInjectTest {
  object AutoVariable extends AutoVariable with AutoInject[AutoVariable] {
    override def test: String = "BBB"
  }

  trait AutoVariable {
    def test: String = "AAA"
  }
}

class AutoInjectTest extends WordSpec with Matchers {
  "Auto inject test" should {
    "the simplest Injection" in {
      object Execute extends Injector {
        def test(): Assertion = {
          inject[AutoVariable].provide shouldBe AutoVariable
        }
      }

      Execute.test()
    }

    "Implicit function reference test" in {
      object Test extends Injector {
        def test = {
          inject[AutoVariable].test shouldBe "BBB"
        }
      }
      Test.test
    }
  }
}