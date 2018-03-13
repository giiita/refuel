package com.giitan.inject

import com.giitan.injector.{AutoInjector, Injector}
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
        depends[A](B).accept(this)
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
            case e: IllegalAccessException => e.getMessage shouldBe "trait A or internal dependencies injected failed. "
            case e: Throwable => throw new Exception("Unknown exception.", e)
          }
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
        override def mount = {
          me.as[I]
        }
      }

      trait CallerExpand extends Caller with K

      new CallerExpand{}.result shouldBe "K"
    }
  }
}

trait I extends AutoInjector {
  def name: String
}
object J extends I {
  def name: String = "J"
  depends[I](this)
}

object Caller extends Caller

trait Caller extends Injector {
  val i: I = inject[I]
  def result: String = i.name
}