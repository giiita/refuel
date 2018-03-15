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

  trait Named {
    def name: Int
  }

  object Named1 extends Named {
    def name = 1
  }

  object Named2 extends Named {
    def name = 2
  }

  "inject" should {

    "out of scope" in {
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
            case e: IllegalAccessException => assert(e.getMessage startsWith "trait A or internal dependencies injected failed.")
            case e: Throwable => throw new Exception("Unknown exception.", e)
          }
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }

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

    "Be full access." in {
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
          inject[A] shouldBe B
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }

    "mount yet." in {

      trait K extends I {me =>
        def name: String = "K"
        depends[I](me)
      }

      trait CallerExpand extends Caller with K

      new Caller{}.result shouldBe "J"
    }

    "mount." in {

      trait K extends I {me =>
        def name: String = "K"
        override def mount(): Unit = {
          me.narrow[I](me)
        }
      }

      trait CallerExpand extends Caller with K

      new CallerExpand{}.result shouldBe "K"
    }
  }
}