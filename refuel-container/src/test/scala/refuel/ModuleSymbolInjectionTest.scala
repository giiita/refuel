package refuel

import org.scalatest.Assertion
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.Types.@@
import refuel.container.anno.{Effective, RecognizedDynamicInjection}
import refuel.domination.Inject
import refuel.domination.InjectionPriority.{Default, Finally, Overwrite}
import refuel.exception.DIAutoInitializationException
import refuel.injector.{AutoInject, Injector}
import refuel.internal.di.Effect
import refuel.provider.{Lazy, Tag}

import scala.util.Try

object ModuleSymbolInjectionTest {


  object TEST1 {

    trait TestIF_1 {
      val value = "value"
    }

    object TestIFImpl_1 extends TestIF_1 with AutoInject

  }

  object TEST2 {

    trait TestIF_2

    object TestIFImpl_2 extends TestIF_2 with AutoInject

  }

  object TEST3 {

    trait TestIF_3

    @Inject(Finally)
    object TestIFImpl_3_RECOVER extends TestIF_3 with AutoInject

    object TestIFImpl_3_AUTO extends TestIF_3 with AutoInject

  }

  object TEST4 {

    trait TestIF_4

    @Inject(Finally)
    object TestIFImpl_4_RECOVER extends TestIF_4 with AutoInject

    @Inject(Overwrite)
    object TestIFImpl_4_CUSTOM extends AutoInject with TestIF_4

  }

  object TEST5 {

    trait TestIF_5

    @Inject(Finally)
    object TestIFImpl_5_RECOVER extends TestIF_5 with AutoInject

    @Inject(Default)
    object TestIFImpl_5_CUSTOM extends AutoInject with TestIF_5

  }

  object TEST7 {

    trait TestIF_7

    object TestIFImpl_7_AUTO extends TestIF_7 with AutoInject

    @Inject(Default)
    object TestIFImpl_7_CUSTOM extends AutoInject with TestIF_7

  }

  object TEST8 {

    trait TestIF_8

    object TestIFImpl_8_AUTO extends TestIF_8 with AutoInject

    @Inject(Overwrite)
    object TestIFImpl_8_CUSTOM extends AutoInject with TestIF_8

  }

  object TEST101 {

    trait TestIF_101

    object TestIFImpl_101_AUTO extends TestIF_101 with AutoInject

  }

  object TEST102 {

    trait TestIF_102

    object TestIFImpl_102_AUTO extends TestIF_102 with AutoInject

  }

  object TEST103 {

    trait TestIF_103

    object TestIFImpl_103_AUTO extends TestIF_103 with AutoInject

  }

  object TEST104 {

    trait TestIF_104

    object TestIFImpl_104_AUTO extends TestIF_104 with AutoInject

  }

  object TEST105 {

    trait TestIF_105

    trait AccessorTest extends Injector {
      def get: Lazy[TestIF_105] = inject[TestIF_105]
    }

    object TestIFImpl_105_AUTO extends TestIF_105 with AutoInject

    object AccessorA extends AccessorTest

    object AccessorB extends AccessorTest

    object AccessorC extends AccessorTest

  }

  object TEST106 {

    trait TestIF_106

    trait AccessorTestA extends Injector {
      def get: Lazy[TestIF_106] = inject[TestIF_106]
    }

    trait AccessorTestB extends Injector {
      def get: Lazy[TestIF_106] = inject[TestIF_106]
    }

    trait AccessorTestC extends Injector {
      def get: Lazy[TestIF_106] = inject[TestIF_106]
    }

    object TestIFImpl_106_AUTO extends TestIF_106 with AutoInject

    object AccessorA extends AccessorTestA

    object AccessorB extends AccessorTestB

    object AccessorC extends AccessorTestC

  }

  object TEST107 {

    trait TestIF_107

    object TestIFImpl_107_AUTO extends TestIF_107 with AutoInject

  }

  object TEST108 {

    trait A108

    trait A108_TRAIT

    trait B108 extends Injector {
      val a: Lazy[A108] = inject[A108]
    }

    trait C108 extends Injector {
      val b: Lazy[B108] = inject[B108]
    }

    class A180_CLASS extends A108_TRAIT with AutoInject

    object A108_REPLACE extends A108

    object A108 extends A108 with AutoInject

    object B108 extends B108 with AutoInject

    object C108 extends C108 with AutoInject

  }

  object TEST109 {

    trait A109 {
      val value: String
    }

    object EffectA extends Effect {
      override def activate: Boolean = false
    }

    object EffectB extends Effect {
      override def activate: Boolean = true
    }

    object EffectC extends Effect {
      override def activate: Boolean = false
    }

    @Effective(EffectA)
    object A109_1 extends A109 with AutoInject {
      val value: String = "A"
    }

    @Effective(EffectB)
    object A109_2 extends A109 with AutoInject {
      val value: String = "B"
    }

    @Effective(EffectC)
    object A109_3 extends A109 with AutoInject {
      val value: String = "C"
    }

  }


  object TEST110 {

    trait A110 {
      val value: String
    }

    object EffectA extends Effect {
      override def activate: Boolean = false
    }

    object EffectB extends Effect {
      override def activate: Boolean = false
    }

    object EffectC extends Effect {
      override def activate: Boolean = false
    }

    @Effective(EffectA)
    object A110_1 extends A110 with AutoInject {
      val value: String = "A"
    }

    @Effective(EffectB)
    object A110_2 extends A110 with AutoInject {
      val value: String = "B"
    }

    @Effective(EffectC)
    object A110_3 extends A110 with AutoInject {
      val value: String = "C"
    }

  }

  object TEST201 {

    trait TestIF_201

    trait TestTagA

    trait TestTagB

    trait TestTagC

    object TestIFImpl_201_TAGNONE extends TestIF_201 with AutoInject

    object TestIFImpl_201_TAGA extends TestIF_201 with Tag[TestTagA] with AutoInject

    object TestIFImpl_201_TAGB extends TestIF_201 with Tag[TestTagB] with AutoInject

  }

  object TEST301 {

    trait TestIF_301

    trait EX_TestIF_301 extends TestIF_301

    object TestIFImpl_301_AUTO extends TestIF_301 with AutoInject

  }

  object TEST302 {

    trait TestIF_302

    trait EX_TestIF_302 extends TestIF_302

    object TestIFImpl_302_AUTO extends EX_TestIF_302 with AutoInject

  }

  object TEST303 {

    @RecognizedDynamicInjection
    trait Wrap_303[+T <: TestIF] {
      val inst: T
    }

    trait TestIF

    trait TestIF_303_A extends TestIF

    trait TestIF_303_B extends TestIF

    object Wrap {
      def apply[T <: TestIF](t: T): Wrap_303[T] = new Wrap_303[T] {
        override val inst: T = t
      }
    }

  }

  object TEST304 {

    trait TestIF_304_A

    trait TestIF_304_B

  }

}

class ModuleSymbolInjectionTest extends AsyncWordSpec with Matchers with Diagrams with Injector {

  trait Context

  import ModuleSymbolInjectionTest._

  "auto inject" should {
    "default auto inject" in {
      import TEST1._
      inject[TestIF_1].value shouldBe "value"
    }

    "recovered inject" in {
      import TEST2._
      inject[TestIF_2]._provide shouldBe TestIFImpl_2
    }

    "recovered vs default auto inject priority" in {
      import TEST3._
      inject[TestIF_3]._provide shouldBe TestIFImpl_3_AUTO
    }

    "recovered vs custom(1) inject priority" in {
      import TEST4._
      inject[TestIF_4]._provide shouldBe TestIFImpl_4_CUSTOM
    }

    "recovered vs custom(0) inject priority == name after win" in {
      import TEST5._
      inject[TestIF_5]._provide != null shouldBe true
    }

    "auto vs custom(1000) inject priority == name after win" in {

      assertDoesNotCompile("inject[TestIF_7]._provide")

    }

    "auto vs custom(1001) inject priority == custom" in {
      import TEST8._
      inject[TestIF_8]._provide shouldBe TestIFImpl_8_CUSTOM
    }
  }

  "narrow" should {
    "auto vs narrow instance" in {
      import TEST101._

      object LOCAL_TestIF_101 extends TestIF_101

      narrow[TestIF_101](LOCAL_TestIF_101).accept(this).indexing()

      inject[TestIF_101]._provide shouldBe LOCAL_TestIF_101
    }

    "out of scope instance" in {
      import TEST102._

      object LOCAL_TestIF_102 extends TestIF_102

      narrow[TestIF_102](LOCAL_TestIF_102).accept(TEST101.TestIFImpl_101_AUTO).indexing()

      inject[TestIF_102]._provide shouldBe TestIFImpl_102_AUTO
    }

    "auto vs narrow class" in {
      import TEST103._

      object LOCAL_TestIF_103 extends TestIF_103

      narrow[TestIF_103](LOCAL_TestIF_103).accept[ModuleSymbolInjectionTest].indexing()

      inject[TestIF_103]._provide shouldBe LOCAL_TestIF_103
    }

    "out of scope class" in {
      import TEST104._

      object LOCAL_TestIF_104 extends TestIF_104

      narrow[TestIF_104](LOCAL_TestIF_104).accept(TEST101.TestIFImpl_101_AUTO).indexing()

      inject[TestIF_104]._provide shouldBe TestIFImpl_104_AUTO
    }

    "add scope of multiple types" in {
      import TEST104._

      object LOCAL_TestIF_104 extends TestIF_104

      Try {
        narrow[TestIF_104](LOCAL_TestIF_104).accept(TEST101.TestIFImpl_101_AUTO).accept[ModuleSymbolInjectionTest].indexing()
      } match {
        case scala.util.Success(_) => fail()
        case scala.util.Failure(exception) => exception.getMessage shouldBe "If you have already authorized any instance, you can not authorize new types."
      }
    }
    "Meny acception instance" in {
      import TEST105._

      object LOCAL_TestIF_105 extends TestIF_105

      narrow[TestIF_105](LOCAL_TestIF_105).accept(AccessorA).accept(AccessorB).indexing()

      AccessorA.get._provide shouldBe LOCAL_TestIF_105
      AccessorB.get._provide shouldBe LOCAL_TestIF_105
      AccessorC.get._provide shouldBe TestIFImpl_105_AUTO
    }
    "Meny acception class" in {
      import TEST106._

      object LOCAL_TestIF_106 extends TestIF_106

      narrow[TestIF_106](LOCAL_TestIF_106).accept[AccessorTestA].accept[AccessorTestB].indexing()

      AccessorA.get._provide shouldBe LOCAL_TestIF_106
      AccessorB.get._provide shouldBe LOCAL_TestIF_106
      AccessorC.get._provide shouldBe TestIFImpl_106_AUTO
    }

    "shade pattern" in {
      import TEST108._

      shade[Assertion] { implicit c =>
        overwrite[A108](A108_REPLACE)
        inject[C108].b.a._provide shouldBe A108_REPLACE
      }

      inject[C108].b.a._provide shouldBe A108
    }

    "Effective injection" in {
      import TEST109._

      inject[A109]._provide shouldBe A109_2
    }

    "Effect is desabled all" in {
      import TEST110._

      Try {
        inject[A110]._provide
      } match {
        case scala.util.Failure(_: DIAutoInitializationException) => succeed
        case _ => fail()
      }
    }
  }

  "Tagging" should {
    "tag inspect" in {
      import TEST201._
      assertDoesNotCompile("inject[TestIF_201]._provide")
      inject[TestIF_201 @@ TestTagA]._provide shouldBe TestIFImpl_201_TAGA
      inject[TestIF_201 @@ TestTagB]._provide shouldBe TestIFImpl_201_TAGB

      // Be compile error at v1.1.0
      assertDoesNotCompile(
        "inject[TestIF_201 @@ TestTagC]._provide"
      )
    }
  }

  "Inheritance relationship" should {
    "pattern 1" in {
      import TEST301._
      inject[TestIF_301]._provide shouldBe TestIFImpl_301_AUTO
      assertDoesNotCompile("inject[EX_TestIF_301]._provide shouldBe TestIFImpl_301_AUTO")
    }
    "pattern 2" in {
      import TEST302._
      inject[TestIF_302]._provide shouldBe TestIFImpl_302_AUTO
    }

    "type erase" in {
      import TEST303._

      val r_A = Wrap(new TestIF_303_A {})

      val r_B = Wrap(new TestIF_303_B {})

      overwrite[Wrap_303[TestIF_303_A]](r_A)
      overwrite[Wrap_303[TestIF_303_B]](r_B)

      import scala.reflect.runtime.universe._
      def get[T <: TestIF : WeakTypeTag]: Lazy[Wrap_303[T]] = inject[Wrap_303[T]]

      get[TestIF_303_A]._provide shouldBe r_A
      get[TestIF_303_B]._provide shouldBe r_B
    }

    "type erase with Seq" in {
      import TEST304._


      type Alias[T] = Seq[T]

      val r_A = Seq(
        new TestIF_304_A {},
        new TestIF_304_A {},
        new TestIF_304_A {}
      )

      val r_B = Seq(
        new TestIF_304_B {},
        new TestIF_304_B {},
        new TestIF_304_B {}
      )

      overwrite[Alias[TestIF_304_A]](r_A)
      overwrite[Alias[TestIF_304_B]](r_B)

      inject[Alias[TestIF_304_A]@RecognizedDynamicInjection]._provide shouldBe r_A
      inject[Alias[TestIF_304_B]@RecognizedDynamicInjection]._provide shouldBe r_B
    }
  }
}
