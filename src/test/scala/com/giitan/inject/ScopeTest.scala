package com.giitan.inject

import com.giitan.exception.InjectableDefinitionException
import com.giitan.inject.ScopeTest._
import com.giitan.injector.{AutoInject, Injector}
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

  object AutoVariable2 extends AutoVariable2 {
    override def test: String = "BBB"
  }

  trait AutoVariable2 extends AutoInject[AutoVariable2] {
    def test: String = "AAA"
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
          inject[A].provide shouldBe B
        }
      }

      object ExecuteY extends InjectorY {
        def test(): Assertion = {
          try {
            inject[A].provide
            throw new Exception("Do not be successful.")
          } catch {
            case _: InjectableDefinitionException => succeed
          }
        }
      }

      ExecuteB.test()
      ExecuteY.test()
    }

    "Override reference without lazy define." in {
      trait OverrideInject extends Injector {
        depends[AutoVariable2](
          new AutoVariable2 {
            override def test: String = "CCC"
          }
        )
      }

      object ExecuteTest extends Injector {
        private val x: AutoVariable2 = inject[AutoVariable2]

        def execute = x.test shouldBe "CCC"
      }

      object Runner extends OverrideInject {
        def run: Assertion = ExecuteTest.execute
      }

      Runner.run
    }
  }
}
