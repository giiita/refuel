package com.giitan.inject

import com.giitan.inject.InjectorTest._
import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}

object InjectorTest {
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

class InjectorTest extends WordSpec with Matchers { me =>

  "Injector test" should {

    "default" in {
      trait InjectorB extends Injector {
        depends[A](B)
      }

      object Execute extends InjectorB {
        def test(): Assertion = {
          inject[A].provide shouldBe B
        }
      }

      Execute.test()
    }

    "Be full access." in {
      trait InjectorB extends Injector {
        depends[A](B)
      }

      trait InjectorY extends Injector {
        depends[X](Y)
      }

      object ExecuteB extends InjectorB {
        def test(): Assertion = {
          inject[A].provide shouldBe B
        }
      }

      object ExecuteY extends InjectorY {
        def test(): Assertion = {
          inject[A].provide shouldBe B
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }
  }
}