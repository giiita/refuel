package com.giitan.inject

import com.giitan.box.ScaladiaClassLoader.classLoader
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

  trait X[A]

  object Y extends X[String] with AutoInject[X[String]]

  object Z extends X[Int] with AutoInject[X[Int]]

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

    "AutoInject with type erase" in {
      object Test extends Injector {
        def testY = {
          inject[X[String]].provide shouldBe Z
        }

        def testZ = {
          inject[X[Int]].provide shouldBe Z
        }
      }
      Test.testY
      Test.testZ
    }

    "TEST" in {
      import scala.reflect._
      import runtime.universe._
      Thread.currentThread().getContextClassLoader.loadClass("com.giitan.inject.AutoInjectTest$Y") shouldBe ""
    }

    "Hoge" in {
      import scala.reflect._
      import runtime.universe._

      def getRecursiveTypeParam(tp: Type): List[Type] = {
        tp.typeArgs.flatMap(x => getRecursiveTypeParam(x).:+(x))
      }

      val result = runtimeMirror(Thread.currentThread().getContextClassLoader)
        .staticClass("com.giitan.inject.AutoInjectTest$Y$")
        .info
        .baseType(typeOf[AutoInject[_]].typeSymbol)

      result.=:=(typeOf[AutoInject[X[Int]]]) shouldBe true

      // .baseClasses(3)
      // .asType.toType


      // getRecursiveTypeParam(result) shouldBe ""
      // Thread.currentThread().getContextClassLoader.loadClass("com.giitan.inject.AutoInjectTest$Y$")

      // Thread.currentThread().getContextClassLoader.loadClass("com.giitan.inject.AutoInjectTest$Y$").getGenericInterfaces shouldBe ""

      // typeOf[AutoVariable].typeArgs shouldBe ""
    }
  }
}