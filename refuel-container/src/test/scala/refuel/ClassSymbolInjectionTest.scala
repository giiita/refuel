package refuel

import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}
import refuel.ClassSymbolInjectionTest.TEST_K.K
import refuel.Types.@@
import refuel.effect.{Effect, Effective}
import refuel.exception.DIAutoInitializationException
import refuel.injector.{AutoInject, InjectOnce, Injector}
import refuel.provider.Tag


object ClassSymbolInjectionTest {

  object TEST_A {

    trait A {
      val v: String
    }

    class AImpl extends A with AutoInject[A] {
      val v: String = "AImpl"
    }

  }

  object TEST_B {

    trait TAG

    trait TAG_A

    trait TAG_B

    trait B {
      val v: String
    }

    class B_TAG_A extends B with AutoInject[B @@ TAG_A] with Tag[TAG_A] {
      val v: String = "TAGGED A"
    }

    class B_TAG_B extends B with AutoInject[B @@ TAG_B] with Tag[TAG_B] {
      val v: String = "TAGGED B"
    }

  }

  object TEST_C {

    object C_Setting {
      val `type` = 2
    }

    object C_EFFECT_A extends Effect {
      override def activate: Boolean = C_Setting.`type` == 1
    }

    object C_EFFECT_B extends Effect {
      override def activate: Boolean = C_Setting.`type` == 2
    }

    object C_EFFECT_C extends Effect {
      override def activate: Boolean = C_Setting.`type` == 3
    }

    trait Effectives {
      val value: String
    }

    @Effective(C_EFFECT_A)
    class C_EFFECTIVE_A() extends Effectives with AutoInject[Effectives] {
      override val value: String = "I am A"
    }

    @Effective(C_EFFECT_B)
    class C_EFFECTIVE_B() extends Effectives with AutoInject[Effectives] {
      override val value: String = "I am B"
    }

    @Effective(C_EFFECT_C)
    class C_EFFECTIVE_C() extends Effectives with AutoInject[Effectives] {
      override val value: String = "I am C"
    }

  }

  object TEST_D {

    trait D {
      val v: String
    }

    class DImpl extends D with AutoInject[D] {
      val v: String = "DImpl"
    }

    object DImplModule extends D with AutoInject[D] {
      val v: String = "DImplModule"
    }

  }

  object TEST_E {

    trait E {
      val v: String
    }

    class EImpl extends E with AutoInject[E] {
      val v: String = "EImpl"
    }

  }

  object TEST_F {

    trait F {
      val v: String
    }

    class FImpl extends F with InjectOnce[F] {
      val v: String = "FImpl"
    }

  }

  object TEST_G {

    trait G_FIRST_PARAM

    object G_FIRST_PARAM_IMPL extends G_FIRST_PARAM with AutoInject[G_FIRST_PARAM]

    trait G_TYPE_PARAM

    case class G_TYPE_PARAM_A() extends G_TYPE_PARAM

    trait G_INNER[T] {
      val t: T
    }

    class G_INNER_IMPL extends G_INNER[G_TYPE_PARAM_A] with AutoInject[G_INNER[G_TYPE_PARAM_A]] {
      val t: G_TYPE_PARAM_A = G_TYPE_PARAM_A()
    }

    trait G {
      val inner: G_INNER[G_TYPE_PARAM_A]
      val first: G_FIRST_PARAM
    }

    class G_IMPL(val first: G_FIRST_PARAM, val inner: G_INNER[G_TYPE_PARAM_A]) extends G with AutoInject[G]

  }

  object TEST_H {

    trait H

    class HImpl extends H with AutoInject[H]

  }

  object TEST_I {

    trait I

    class IImpl() extends I with AutoInject[I]

  }

  object TEST_J {

    trait J

    case class JImpl() extends J with AutoInject[J]

    type J_ALIAS = J

    case class JAliasImpl() extends J_ALIAS with AutoInject[J_ALIAS]

  }

  object TEST_K {

    trait K

    case class KImpl(vvv: String) extends K with AutoInject[K]

  }

  object TEST_L {

    trait L {
      val value: LInner
    }
    trait LInner

    case class LImpl(value: LInner) extends L with AutoInject[L]

  }

}

class ClassSymbolInjectionTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with Injector {
  "confirm" should {
    "TEST" in {
      class A
      type B = A

      import scala.reflect.runtime.universe._
      val tag = weakTypeTag[B]
      succeed
    }

    "No module, one class" in {
      import refuel.ClassSymbolInjectionTest.TEST_A._
      bind[A].v shouldBe "AImpl"
    }

    "Many tag type" in {
      import refuel.ClassSymbolInjectionTest.TEST_B._
      bind[B @@ TAG_A].v shouldBe "TAGGED A"
      bind[B @@ TAG_B].v shouldBe "TAGGED B"
    }

    "Auto Injectable class symbol inject become a different instance" in {
      import refuel.ClassSymbolInjectionTest.TEST_I._
      bind[I] should not be bind[I]
    }

    "If primary constructor has parameters, the parameters is confirmed" in {
      import refuel.ClassSymbolInjectionTest.TEST_G._
      val result = bind[G]
      result.inner.t shouldBe G_TYPE_PARAM_A()
      result.first shouldBe G_FIRST_PARAM_IMPL
    }
  }

  "inject" should {
    "Effective injection" in {
      import refuel.ClassSymbolInjectionTest.TEST_C._
      inject[Effectives].value shouldBe "I am B"
    }
    "Auto injectable class vs module" in {
      import refuel.ClassSymbolInjectionTest.TEST_D._
      inject[D].v shouldBe "DImplModule"
    }
    "class symbol inject with flush container" in {
      import refuel.ClassSymbolInjectionTest.TEST_E._
      val a = inject[E]._provide
      val b = inject[E]._provide
      a shouldBe b
    }
    "class symbol inject no flush" in {
      import refuel.ClassSymbolInjectionTest.TEST_F._
      val a = inject[F]._provide
      val b = inject[F]._provide
      a should not be b
    }

    "If primary constructor has parameters, inject[T] is recursive inject" in {
      import refuel.ClassSymbolInjectionTest.TEST_G._

      val result = inject[G]._provide
      result.inner.t shouldBe G_TYPE_PARAM_A()
      result.first shouldBe G_FIRST_PARAM_IMPL
    }

    "If primary constructor has parameters, and not found target, inject[T] is fail" in {
      import refuel.ClassSymbolInjectionTest.TEST_K._

      try {
        inject[K]._provide
        fail()
      } catch {
        case e: DIAutoInitializationException =>
          succeed
        case e: Throwable => fail(e.getMessage)
      }
    }

    "If primary constructor has parameters, and bounds are specified, switched inject result." in {
      import refuel.ClassSymbolInjectionTest.TEST_L._

      val inner1 = new LInner {
      }
      val inner2 = new LInner {
      }
      val inner3 = new LInner {
      }
      narrow[LInner](inner2).accept[L].indexing()
      inject[L]._provide.value shouldBe inner2
    }
  }

  "shade" should {
    "Auto Injectable class symbol inject become a different instance in shade" in {
      import refuel.ClassSymbolInjectionTest.TEST_H._
      val pre = inject[H]._provide
      pre shouldBe inject[H]._provide

      shade { implicit c =>
        pre should not be inject[H]._provide
      }

      pre shouldBe inject[H]._provide
    }
  }

  //  "type alias injection" should {
  //    "Can inject each alias" in {
  //      import refuel.ClassSymbolInjectionTest.TEST_J._
  //      inject[J]._provide shouldBe JImpl()
  //      inject[J_ALIAS]._provide shouldBe JAliasImpl()
  //    }
  //  }
}
