package com.giitan.inject

import com.giitan.exception.InjectableDefinitionException
import com.giitan.inject.ScopeTest._
import com.giitan.injector.{AutoInject, Injector}
import org.scalatest.{Assertion, Matchers, WordSpec}

object ScopeTest {

  trait A

  trait X

  trait Named {
    def name: Int
  }

  trait AutoVariable2 extends AutoInject[AutoVariable2] {
    def test: String = "AAA"
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

  object AutoVariable2 extends AutoVariable2 {
    override def test: String = "BBB"
  }

}

class ScopeTest extends WordSpec with Matchers {
  "Scope test" should {
    "Out of dynamic scope" in {
      trait InjectorB extends Injector {
        narrow[ScopeTest.A](ScopeTest.B).accept(this).indexing()
      }

      trait InjectorY extends Injector {
        depends[X](Y)
      }

      object ExecuteB extends InjectorB {
        def test(): Assertion = {
          inject[ScopeTest.A].provide shouldBe ScopeTest.B
        }
      }

      object ExecuteY extends InjectorY {
        def test(): Assertion = {
          try {
            inject[ScopeTest.A].provide
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

    "SubType scope accessible." in {
      trait InjectorB extends Injector {
        narrow[ScopeTest.A](ScopeTest.B).accept(this).indexing()
      }

      trait CushionType extends InjectorB

      object ExecuteB extends CushionType {
        def test(): Assertion = {
          inject[ScopeTest.A].provide shouldBe ScopeTest.B
        }
      }

      ExecuteB.test()
    }

    "Can access from anything." in new Injector {

      object D extends Injector {
        def test: ScopeTest.A = inject[ScopeTest.A]
      }

      object F extends ScopeTest.A

      object G extends ScopeTest.A

      depends[ScopeTest.A](G)
      narrow[ScopeTest.A](F).accept(D).indexing()

      D.test shouldBe F
      inject[ScopeTest.A].provide shouldBe G
    }

  }

  "The reference order is ObjectScopeDependency > ClassScopeDependency > GlobalDependency" should {
    "The regist order is ObjectScopeDependency -> ClassScopeDependency -> GlobalDependency" in new Injector {

      import ScopeTest_CASE1._

      narrow[A](new LOCAL).accept(this).indexing()
      narrow[A](new CLASS).accept[Access1].indexing()
      depends[A](new GLOBAL)

      inject[A].value shouldBe "LOCAL"
      new Access1().value shouldBe "CLASS"
      new Access2().value shouldBe "GLOBAL"
    }
    "The regist order is GlobalDependency -> ClassScopeDependency -> ObjectScopeDependency" in new Injector {

      import ScopeTest_CASE1._

      depends[A](new GLOBAL)
      narrow[A](new CLASS).accept[Access1].indexing()
      narrow[A](new LOCAL).accept(this).indexing()

      inject[A].value shouldBe "LOCAL"
      new Access1().value shouldBe "CLASS"
      new Access2().value shouldBe "GLOBAL"
    }
    "If can access type dependencies Already exist, be erased existing dependencies" in new Injector {

      import ScopeTest_CASE1._

      narrow[A](new CLASS).accept[Access1].indexing()
      narrow[A](new GLOBAL).accept[Access1].indexing()
      narrow[A](new LOCAL).accept[Access1].indexing()

      new Access1().value shouldBe "LOCAL"
    }
    "If can access object dependencies Already exist, be erased existing dependencies" in new Injector {

      import ScopeTest_CASE1._

      val accessFrom = new Access1

      narrow[A](new CLASS).accept(accessFrom).indexing()
      narrow[A](new GLOBAL).accept(accessFrom).indexing()
      narrow[A](new LOCAL).accept(accessFrom).indexing()

      accessFrom.value shouldBe "LOCAL"
    }
    "Cannot access other point." in new Injector {

      import ScopeTest_CASE2._

      val accessFrom = new Access1

      narrow[A](new LOCAL).accept(accessFrom).indexing()

      new Access1().value shouldBe "GLOBAL"
    }
  }
}

object ScopeTest_CASE1 {

  trait A {
    val value: String
  }

  class LOCAL extends A {
    val value: String = "LOCAL"
  }

  class CLASS extends A {
    val value: String = "CLASS"
  }

  class GLOBAL extends A {
    val value: String = "GLOBAL"
  }

  class Access1 extends Injector {
    def value = inject[A].value
  }

  class Access2 extends Injector {
    def value = inject[A].value
  }

  class Access3 extends Injector {
    def value = inject[A].value
  }
}

object ScopeTest_CASE2 {

  trait A {
    val value: String
  }

  class LOCAL extends A {
    val value: String = "LOCAL"
  }

  object GLOBAL extends A with AutoInject[A] {
    val value: String = "GLOBAL"
  }

  class Access1 extends Injector {
    def value = inject[A].value
  }
}
