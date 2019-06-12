package com.giitan.inject

import com.giitan.inject.InjectorTest._
import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}

import scala.util.{Failure, Success, Try}

object InjectorTest {

  trait AAA

  trait XXX

  trait Named {
    def name: Int
  }

  object BBB extends AAA

  object CCC extends AAA

  object YYY extends XXX

  object ZZZ extends XXX

  object Named1 extends Named {
    def name = 1
  }

  object Named2 extends Named {
    def name = 2
  }

}

object InjectorTest_RECOVER_CASE {
  trait EEE
  object FFF extends EEE
}

class InjectorTest extends WordSpec with Matchers {
  me =>

  "Injector test" should {

    "default" in {
      trait InjectorB extends Injector {
        depends[InjectorTest.AAA](InjectorTest.BBB)
      }

      object Execute extends InjectorB {
        def test(): Assertion = {
          inject[InjectorTest.AAA].provide shouldBe InjectorTest.BBB
        }
      }

      Execute.test()
    }

    "Be full access." in {
      trait InjectorB extends Injector {
        depends[InjectorTest.AAA](InjectorTest.BBB)
      }

      trait InjectorY extends Injector {
        depends[XXX](YYY)
      }

      object ExecuteB extends InjectorB {
        def test(): Assertion = {
          inject[AAA].provide shouldBe BBB
        }
      }

      object ExecuteY extends InjectorY {
        def test(): Assertion = {
          inject[AAA].provide shouldBe BBB
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }

    "Un recovery injection" in new Injector {
      Try {
        inject[InjectorTest_RECOVER_CASE.EEE].provide
      } match {
        case Success(_) => fail()
        case Failure(_) => succeed
      }
    }

    "Recovery injection" in new Injector {
      inject[InjectorTest_RECOVER_CASE.EEE].recover {
        case _ => InjectorTest_RECOVER_CASE.FFF
      }.provide shouldBe InjectorTest_RECOVER_CASE.FFF
    }
  }
}