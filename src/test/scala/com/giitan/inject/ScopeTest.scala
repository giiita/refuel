package com.giitan.inject

import com.giitan.exception.InjectableDefinitionException
import com.giitan.inject.ScopeTest._
import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}

object ScopeTest {
  trait A
  object B extends A
  object C extends A

  trait X
  object Y extends X
  object Z extends X

  trait Named {
    def name: Int
  }

  object Named1 extends Named {
    def name = 1
  }

  object Named2 extends Named {
    def name = 2
  }
}

class ScopeTest extends WordSpec with Matchers {
  "Scope test" should {

    "Out of dynamic scope" in {
      trait InjectorB extends Injector {
        narrow[A](B)
      }

      trait InjectorY extends Injector {
        depends[X](Y)
      }

      object ExecuteB extends InjectorB {
        def test(): Assertion = {
          inject[A] shouldBe B
        }
      }

      object ExecuteY extends InjectorY {
        def test(): Assertion = {
          try {
            inject[A]
            throw new Exception("Do not be successful.")
          } catch {
            case _: InjectableDefinitionException => succeed
          }
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }
  }
}
