package com.giitan.inject

import com.giitan.injector.Injector
import org.scalatest.{Assertion, Matchers, WordSpec}

class InjectorTest extends WordSpec with Matchers {

  trait A
  object B extends A
  object C extends A

  trait X
  object Y extends X
  object Z extends X

  "inject" should {

    "default" in {
      trait InjectorB extends Injector {
        depends[A](B)
      }

      object Execute extends InjectorB {
        def test(): Assertion = {
          inject[A] shouldBe B
        }
      }

      Execute.test()
    }

    "out of scope" in {
      trait InjectorB extends Injector {
        depends[A](B)
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
            case e: IllegalAccessException => e.getMessage shouldBe "Uninjectable object. trait A"
            case _ => throw new Exception("Unknown exception.")
          }
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }

    "Append scope." in {
      trait InjectorB extends Injector {
        depends[A](B)
          .accept(ExecuteY)
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
          inject[A] shouldBe B
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }

    "Be full access." in {
      trait InjectorB extends Injector {
        depends[A](B)
          .acceptedGlobal
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
          inject[A] shouldBe B
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }
  }
}