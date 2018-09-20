package com.giitan.inject

import com.giitan.inject.InjectorTest.{A, _}
import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}

import scala.util.{Failure, Success, Try}

object InjectorTest {

  trait A

  trait X

  trait Named {
    def name: Int
  }

  object B extends A

  object C extends A

  object Y extends X

  object Z extends X

  object Named1 extends Named {
    def name = 1
  }

  object Named2 extends Named {
    def name = 2
  }

}

object InjectorTest_RECOVER_CASE {
  trait A
  object B extends A
}

class InjectorTest extends WordSpec with Matchers {
  me =>

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

    "Un recovery injection" in new Injector {
      Try {
        inject[InjectorTest_RECOVER_CASE.A].provide
      } match {
        case Success(_) => fail()
        case Failure(_) => succeed
      }
    }

    "Recovery injection" in new Injector {
      inject[InjectorTest_RECOVER_CASE.A].recover {
        case _ => InjectorTest_RECOVER_CASE.B
      }.provide shouldBe InjectorTest_RECOVER_CASE.B
    }
  }
}