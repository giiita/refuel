package refuel

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.container.anno.RecognizedDynamicInjection
import refuel.exception.DIAutoInitializationException
import refuel.injector.{AutoInject, Injector}

object PrimaryConstructorInjectionTest {

  object TEST1 {

    object Wrap {

      class D1(d2: D2) extends AutoInject

    }

    class D2(internal: InternalDep)(implicit i1: Implicit1, i2: Implicit2) extends AutoInject

    class InternalDep extends AutoInject

    class Implicit1

    class Implicit2

    object Implicits {
      implicit val _i1 = new Implicit1
      implicit val _i2 = new Implicit2
    }
  }
  object SAME_AS_TEST1 {

    object Wrap {

      class D1(d2: D2) extends AutoInject

    }

    class D2(internal: InternalDep)(implicit i1: Implicit1, i2: Implicit2) extends AutoInject

    class InternalDep extends AutoInject

    class Implicit1
    class Implicit2

    object Implicits {
      implicit val _i1 = new Implicit1
      implicit val _i2 = new Implicit2
    }
  }

  object RUNTIME_PC_INJECT {
    class A extends AutoInject
    class B extends AutoInject
    class C extends AutoInject
    class D extends AutoInject

    case class ROOT(a: A, b: B)(c: C, d: D) extends AutoInject {
      def get: (A, B, C, D) = (a, b, c, d)
    }
  }
}

class PrimaryConstructorInjectionTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  "primary constructor injection" should {
    "No found implicit param at implicit scope" in {
      assertDoesNotCompile("""
           import refuel.PrimaryConstructorInjectionTest.TEST1._
           inject[D1]._provide should not be(null)
         """)
    }
    "Not injected implicit parameters." in {
      import refuel.PrimaryConstructorInjectionTest.TEST1._
      import refuel.PrimaryConstructorInjectionTest.TEST1.Implicits._
      inject[Wrap.D1]._provide should not be (null)
    }
    "Can injected implicit parameters at Runtime when shade and indexing" in {
      import refuel.PrimaryConstructorInjectionTest.SAME_AS_TEST1._
      import refuel.PrimaryConstructorInjectionTest.SAME_AS_TEST1.Implicits._
      shade { implicit c =>
        _i1.index[Implicit1]()
        _i2.index[Implicit2]()
        inject[Wrap.D1 @RecognizedDynamicInjection]._provide should not be (null)
      }
    }
    "Runtime multiple param set injection" in {
      import refuel.PrimaryConstructorInjectionTest.RUNTIME_PC_INJECT._
      val xxx = inject[ROOT @RecognizedDynamicInjection]._provide.get
      inject[ROOT @RecognizedDynamicInjection]._provide should not be (null)
    }
  }
}
