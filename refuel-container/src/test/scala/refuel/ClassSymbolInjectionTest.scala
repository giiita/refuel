package refuel

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.container.anno.{Effective, RecognizedDynamicInjection}
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Primary
import refuel.injector.{AutoInject, Injector}
import refuel.internal.di.Effect


object ClassSymbolInjectionTest {

  object TEST_A {

    trait A {
      def bool: Boolean
    }

    case object A_TRUE extends A with AutoInject {
      override def bool: Boolean = true
    }

    case object A_EFF_A extends Effect with Injector {
      override def activate: Boolean = inject[A].bool
    }

    case object A_EFF_B extends Effect with Injector {
      override def activate: Boolean = !inject[A].bool
    }

    case object A_EFF_C extends Effect with Injector {
      override def activate: Boolean = inject[A].bool
    }

    trait A_EFFECTIVE_IF

    @Effective(A_EFF_A)
    class A_EFFECTIVE1 extends A_EFFECTIVE_IF with AutoInject {
      val v: String = "A"
    }

    @Effective(A_EFF_B)
    class A_EFFECTIVE2 extends A_EFFECTIVE_IF with AutoInject {
      val v: String = "B"
    }

    @Effective(A_EFF_C)
    class A_EFFECTIVE3 extends A_EFFECTIVE_IF with AutoInject {
      val v: String = "C"
    }

    @Effective(A_EFF_A)
    @Inject(Primary)
    class A_EFFECTIVE4 extends A_EFFECTIVE_IF with AutoInject {
      val v: String = "A"
    }

    @Effective(A_EFF_B)
    @Inject(Primary)
    class A_EFFECTIVE5 extends A_EFFECTIVE_IF with AutoInject {
      val v: String = "B"
    }
  }

//  object TEST_B {
//    val depend: Seq[Int] = Seq(11)
//  }
//
//  object TEST_C {
//
//    object C_Setting {
//      val `type` = 2
//    }
//
//    object C_EFFECT_A extends Effect {
//      override def activate: Boolean = C_Setting.`type` == 1
//    }
//
//    object C_EFFECT_B extends Effect {
//      override def activate: Boolean = C_Setting.`type` == 2
//    }
//
//    object C_EFFECT_C extends Effect {
//      override def activate: Boolean = C_Setting.`type` == 3
//    }
//
//    trait Effectives {
//      val value: String
//    }
//
//    @Effective(C_EFFECT_A)
//    class C_EFFECTIVE_A() extends Effectives with AutoInject {
//      override val value: String = "I am A"
//    }
//
//    @Effective(C_EFFECT_B)
//    class C_EFFECTIVE_B() extends Effectives with AutoInject {
//      override val value: String = "I am B"
//    }
//
//    @Effective(C_EFFECT_C)
//    class C_EFFECTIVE_C() extends Effectives with AutoInject {
//      override val value: String = "I am C"
//    }
//
//  }
//
//  object TEST_D {
//
//    trait D {
//      val v: String
//    }
//
//    class DImpl extends D with AutoInject {
//      val v: String = "DImpl"
//    }
//
//    object DImplModule extends D with AutoInject {
//      val v: String = "DImplModule"
//    }
//
//  }
//
//  object TEST_E {
//
//    trait E {
//      val v: String
//    }
//
//    class EImpl extends E with AutoInject {
//      val v: String = "EImpl"
//    }
//
//  }
//
//  object TEST_G {
//
//    trait G_FIRST_PARAM
//
//    object G_FIRST_PARAM_IMPL extends G_FIRST_PARAM with AutoInject
//
//    trait G_TYPE_PARAM
//
//    case class G_TYPE_PARAM_A() extends G_TYPE_PARAM
//
//    trait G_INNER[T] {
//      val t: T
//    }
//
//    class G_INNER_IMPL extends G_INNER[G_TYPE_PARAM_A] with AutoInject {
//      val t: G_TYPE_PARAM_A = G_TYPE_PARAM_A()
//    }
//
//    trait G {
//      val inner: G_INNER[G_TYPE_PARAM_A]
//      val first: G_FIRST_PARAM
//    }
//
//    class G_IMPL(val first: G_FIRST_PARAM, val inner: G_INNER[G_TYPE_PARAM_A]) extends G with AutoInject
//
//  }
//
//  object TEST_H {
//
//    trait H
//
//    class HImpl extends H with AutoInject
//
//  }
//
//  object TEST_I {
//
//    trait I
//
//    class IImpl() extends I with AutoInject
//
//  }
//
//  object TEST_J {
//
//    trait J
//
//    case class JImpl() extends J with AutoInject
//
//    type J_ALIAS = J
//
//    case class JAliasImpl() extends J_ALIAS with AutoInject
//
//  }
//
//  object TEST_K {
//
//    trait K
//
//    case class KImpl(vvv: String) extends K with AutoInject
//
//  }
//
//  object TEST_L {
//
//    trait L {
//      val value: LInner
//    }
//
//    @RecognizedDynamicInjection
//    trait LInner
//
//    case class LImpl(value: LInner) extends L with AutoInject
//
//  }
//
//  object TEST_M {
//
//    case class M() extends AutoInject
//
//  }

}

class ClassSymbolInjectionTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  "inject" should {
    "Effective injection vs Modify injection priority" in {
      import refuel.ClassSymbolInjectionTest.TEST_A._
      inject[A_EFFECTIVE_IF]._provide.isInstanceOf[A_EFFECTIVE4] shouldBe true
    }
    "Reserved type injection" in {
      overwrite(Seq(11))
      inject[Seq[Int]@RecognizedDynamicInjection]._provide shouldBe Seq(11)
    }
//    "Effective injection" in {
//      import refuel.ClassSymbolInjectionTest.TEST_C._
//      inject[Effectives].value shouldBe "I am B"
//    }
//    "Auto injectable class vs module" in {
//      assertDoesNotCompile("inject[D]._provide")
//    }
//    "class symbol inject with flush container" in {
//      import refuel.ClassSymbolInjectionTest.TEST_E._
//      val a = inject[E]._provide
//      val b = inject[E]._provide
//      a shouldBe b
//    }
//
//    "If primary constructor has parameters, inject[T] is recursive inject" in {
//      import refuel.ClassSymbolInjectionTest.TEST_G._
//
//      val result = inject[G]._provide
//      result.inner.t shouldBe G_TYPE_PARAM_A()
//      result.first shouldBe G_FIRST_PARAM_IMPL
//    }
//
//    "If primary constructor has parameters, and not found target, inject[T] is fail" in {
//      assertDoesNotCompile("inject[K]._provide")
//    }
//
//    "If primary constructor has parameters, and bounds are specified, switched inject result." in {
//      import refuel.ClassSymbolInjectionTest.TEST_L._
//
//      val inner = new LInner {
//      }
//      narrow[LInner](inner).accept(this).indexing()
//      inject[L]._provide.value shouldBe inner
//    }
//
//    "Unused interfaces." in {
//      import refuel.ClassSymbolInjectionTest.TEST_M._
//
//      inject[M]._provide shouldBe M()
//    }
  }

//  "shade" should {
//    "Auto Injectable class symbol inject become a different instance in shade" in {
//      import refuel.ClassSymbolInjectionTest.TEST_H._
//      val pre = inject[H]._provide
//      pre shouldBe inject[H]._provide
//
//      shade { implicit c =>
//        pre should not be inject[H]._provide
//      }
//
//      pre shouldBe inject[H]._provide
//    }
//  }
}
