package com.phylage.scaladia

import com.phylage.scaladia.Types.@@
import com.phylage.scaladia.exception.DIAutoInitializationException
import com.phylage.scaladia.injector.{AutoInject, AutoInjectCustomPriority, Injector, RecoveredInject}
import com.phylage.scaladia.provider.{Lazy, Tag}
import org.scalatest.{Assertion, AsyncWordSpec, DiagrammedAssertions, Matchers}

import scala.util.Try

object InjectionTest {


  object TEST1 {

    trait TestIF_1 {
      val value = "value"
    }

    object TestIFImpl_1 extends TestIF_1 with AutoInject[TestIF_1]

  }

  object TEST2 {

    trait TestIF_2

    object TestIFImpl_2 extends TestIF_2 with RecoveredInject[TestIF_2]

  }

  object TEST3 {

    trait TestIF_3

    object TestIFImpl_3_RECOVER extends TestIF_3 with RecoveredInject[TestIF_3]

    object TestIFImpl_3_AUTO extends TestIF_3 with AutoInject[TestIF_3]

  }

  object TEST4 {

    trait TestIF_4

    object TestIFImpl_4_RECOVER extends TestIF_4 with RecoveredInject[TestIF_4]

    object TestIFImpl_4_CUSTOM extends AutoInjectCustomPriority[TestIF_4](1) with TestIF_4

  }

  object TEST5 {

    trait TestIF_5

    object TestIFImpl_5_RECOVER extends TestIF_5 with RecoveredInject[TestIF_5]

    object TestIFImpl_5_CUSTOM extends AutoInjectCustomPriority[TestIF_5](0) with TestIF_5

  }

  object TEST6 {

    trait TestIF_6

    object TestIFImpl_6_AUTO extends TestIF_6 with AutoInject[TestIF_6]

    object TestIFImpl_6_CUSTOM extends AutoInjectCustomPriority[TestIF_6](999) with TestIF_6

  }

  object TEST7 {

    trait TestIF_7

    object TestIFImpl_7_AUTO extends TestIF_7 with AutoInject[TestIF_7]

    object TestIFImpl_7_CUSTOM extends AutoInjectCustomPriority[TestIF_7](1000) with TestIF_7

  }

  object TEST8 {

    trait TestIF_8

    object TestIFImpl_8_AUTO extends TestIF_8 with AutoInject[TestIF_8]

    object TestIFImpl_8_CUSTOM extends AutoInjectCustomPriority[TestIF_8](1001) with TestIF_8

  }

  object TEST101 {

    trait TestIF_101

    object TestIFImpl_101_AUTO extends TestIF_101 with AutoInject[TestIF_101]

  }

  object TEST102 {

    trait TestIF_102

    object TestIFImpl_102_AUTO extends TestIF_102 with AutoInject[TestIF_102]

  }

  object TEST103 {

    trait TestIF_103

    object TestIFImpl_103_AUTO extends TestIF_103 with AutoInject[TestIF_103]

  }

  object TEST104 {

    trait TestIF_104

    object TestIFImpl_104_AUTO extends TestIF_104 with AutoInject[TestIF_104]

  }

  object TEST105 {

    trait TestIF_105

    object TestIFImpl_105_AUTO extends TestIF_105 with AutoInject[TestIF_105]

    trait AccessorTest extends Injector {
      def get = inject[TestIF_105]
    }

    object AccessorA extends AccessorTest

    object AccessorB extends AccessorTest

    object AccessorC extends AccessorTest

  }

  object TEST106 {

    trait TestIF_106

    object TestIFImpl_106_AUTO extends TestIF_106 with AutoInject[TestIF_106]

    trait AccessorTestA extends Injector {
      def get: Lazy[TestIF_106] = inject[TestIF_106]
    }

    trait AccessorTestB extends Injector {
      def get: Lazy[TestIF_106] = inject[TestIF_106]
    }

    trait AccessorTestC extends Injector {
      def get: Lazy[TestIF_106] = inject[TestIF_106]
    }

    object AccessorA extends AccessorTestA

    object AccessorB extends AccessorTestB

    object AccessorC extends AccessorTestC

  }

  object TEST107 {

    trait TestIF_107

    object TestIFImpl_107_AUTO extends TestIF_107 with AutoInject[TestIF_107]

  }

  object TEST108 {

    trait A108
    trait A108_TRAIT
    class A180_CLASS extends A108_TRAIT with AutoInject[A108_TRAIT]

    object A108_REPLACE extends A108

    object A108 extends A108 with AutoInject[A108]

    trait B108 extends Injector {
      val a = inject[A108]
    }

    object B108 extends B108 with AutoInject[B108]

    trait C108 extends Injector {
      val b = inject[B108]
    }

    object C108 extends C108 with AutoInject[C108]

  }

  object TEST201 {

    trait TestIF_201

    trait TestTagA

    trait TestTagB

    trait TestTagC

    object TestIFImpl_201_TAGNONE extends TestIF_201 with AutoInject[TestIF_201]

    object TestIFImpl_201_TAGA extends TestIF_201 with Tag[TestTagA] with AutoInject[TestIF_201 @@ TestTagA]

    object TestIFImpl_201_TAGB extends TestIF_201 with Tag[TestTagB] with AutoInject[TestIF_201 @@ TestTagB]

  }

  object TEST301 {

    trait TestIF_301

    trait EX_TestIF_301 extends TestIF_301

    object TestIFImpl_301_AUTO extends TestIF_301 with AutoInject[TestIF_301]

  }

  object TEST302 {

    trait TestIF_302

    trait EX_TestIF_302 extends TestIF_302

    object TestIFImpl_302_AUTO extends EX_TestIF_302 with AutoInject[TestIF_302]

  }

  object TEST303 {

    object Wrap {
      def apply[T <: TestIF](t: T) = new Wrap_303[T] {
        override val inst: T = t
      }
    }

    trait Wrap_303[+T <: TestIF] {
      val inst: T
    }

    trait TestIF

    trait TestIF_303_A extends TestIF

    trait TestIF_303_B extends TestIF

  }

  object TEST304 {

    trait TestIF_304_A

    trait TestIF_304_B

  }

  object TEST305 {

    import scala.reflect.runtime.universe.WeakTypeTag

    abstract class Wrap_305[+T <: TestIF : WeakTypeTag] {
      val inst: T
    }

    trait TestIF

    trait TestIF_305_A extends TestIF

    object TestIF_305_A_WRAP extends Wrap_305[TestIF_305_A] with AutoInject[Wrap_305[TestIF_305_A]] {
      val inst: TestIF_305_A = new TestIF_305_A {}
    }

    trait TestIF_305_B extends TestIF

    object TestIF_305_B_WRAP extends Wrap_305[TestIF_305_B] with AutoInject[Wrap_305[TestIF_305_B]] {
      val inst: TestIF_305_B = new TestIF_305_B {}
    }

  }

}

class InjectionTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with Injector {

  trait Context

  import InjectionTest._

  "auto inject" should {
    "default auto inject" in {
      import TEST1._
      inject[TestIF_1].value shouldBe "value"
    }

    "recovered inject" in {
      import TEST2._
      inject[TestIF_2].provide shouldBe TestIFImpl_2
    }

    "recovered vs default auto inject priority" in {
      import TEST3._
      inject[TestIF_3].provide shouldBe TestIFImpl_3_AUTO
    }

    "recovered vs custom(1) inject priority" in {
      import TEST4._
      inject[TestIF_4].provide shouldBe TestIFImpl_4_CUSTOM
    }

    "recovered vs custom(0) inject priority == name after win" in {
      import TEST5._
      inject[TestIF_5].provide shouldBe TestIFImpl_5_RECOVER
    }

    "auto vs custom(999) inject priority == auto" in {
      import TEST6._
      inject[TestIF_6].provide shouldBe TestIFImpl_6_AUTO
    }

    "auto vs custom(1000) inject priority == name after win" in {
      import TEST7._
      inject[TestIF_7].provide shouldBe TestIFImpl_7_CUSTOM
    }

    "auto vs custom(1001) inject priority == custom" in {
      import TEST8._
      inject[TestIF_8].provide shouldBe TestIFImpl_8_CUSTOM
    }
  }

  "narrow" should {
    "auto vs narrow instance" in {
      import TEST101._

      object LOCAL_TestIF_101 extends TestIF_101

      narrow[TestIF_101](LOCAL_TestIF_101).accept(this).indexing()

      inject[TestIF_101].provide shouldBe LOCAL_TestIF_101
    }

    "out of scope instance" in {
      import TEST102._

      object LOCAL_TestIF_102 extends TestIF_102

      narrow[TestIF_102](LOCAL_TestIF_102).accept(TEST101.TestIFImpl_101_AUTO).indexing()

      inject[TestIF_102].provide shouldBe TestIFImpl_102_AUTO
    }

    "auto vs narrow class" in {
      import TEST103._

      object LOCAL_TestIF_103 extends TestIF_103

      narrow[TestIF_103](LOCAL_TestIF_103).accept[InjectionTest].indexing()

      inject[TestIF_103].provide shouldBe LOCAL_TestIF_103
    }

    "out of scope class" in {
      import TEST104._

      object LOCAL_TestIF_104 extends TestIF_104

      narrow[TestIF_104](LOCAL_TestIF_104).accept(TEST101.TestIFImpl_101_AUTO).indexing()

      inject[TestIF_104].provide shouldBe TestIFImpl_104_AUTO
    }

    "add scope of multiple types" in {
      import TEST104._

      object LOCAL_TestIF_104 extends TestIF_104

      Try {
        narrow[TestIF_104](LOCAL_TestIF_104).accept(TEST101.TestIFImpl_101_AUTO).accept[InjectionTest].indexing()
      } match {
        case scala.util.Success(_) => fail()
        case scala.util.Failure(exception) => exception.getMessage shouldBe "If you have already authorized any instance, you can not authorize new types."
      }
    }
    "Meny acception instance" in {
      import TEST105._

      object LOCAL_TestIF_105 extends TestIF_105

      narrow[TestIF_105](LOCAL_TestIF_105).accept(AccessorA).accept(AccessorB).indexing()

      AccessorA.get.provide shouldBe LOCAL_TestIF_105
      AccessorB.get.provide shouldBe LOCAL_TestIF_105
      AccessorC.get.provide shouldBe TestIFImpl_105_AUTO
    }
    "Meny acception class" in {
      import TEST106._

      object LOCAL_TestIF_106 extends TestIF_106

      narrow[TestIF_106](LOCAL_TestIF_106).accept[AccessorTestA].accept[AccessorTestB].indexing()

      AccessorA.get.provide shouldBe LOCAL_TestIF_106
      AccessorB.get.provide shouldBe LOCAL_TestIF_106
      AccessorC.get.provide shouldBe TestIFImpl_106_AUTO
    }

    "confirm vs narrow class" in {
      import TEST107._

      object LOCAL_TestIF_107 extends TestIF_107

      val inspection = confirm[TestIF_107]

      narrow[TestIF_107](LOCAL_TestIF_107).accept[InjectionTest].indexing()

      inspection shouldBe TestIFImpl_107_AUTO
    }

    "using pattern" in {
      import TEST108._

      shade[Assertion] { implicit c =>
        overwrite[A108](A108_REPLACE)
        inject[C108].provide.b.a.provide shouldBe A108_REPLACE
      }

      inject[C108].provide.b.a.provide shouldBe A108
    }
  }

  "Tagging" should {
    "tag inspect" in {
      import TEST201._
      inject[TestIF_201].provide shouldBe TestIFImpl_201_TAGNONE
      inject[TestIF_201 @@ TestTagA].provide shouldBe TestIFImpl_201_TAGA
      inject[TestIF_201 @@ TestTagB].provide shouldBe TestIFImpl_201_TAGB

      Try {
        inject[TestIF_201 @@ TestTagC].provide
      } match {
        case scala.util.Success(_) => fail()
        case scala.util.Failure(exception) => exception.getMessage shouldBe "com.phylage.scaladia.Types.<refinement> or its internal initialize failed."
      }
    }
  }

  "Type erase" should {

  }

  "Inheritance relationship" should {
    "pattern 1" in {
      import TEST301._
      inject[TestIF_301].provide shouldBe TestIFImpl_301_AUTO
      Try {
        inject[EX_TestIF_301].provide shouldBe TestIFImpl_301_AUTO
      } match {
        case scala.util.Failure(_: DIAutoInitializationException) => succeed
        case _ => fail()
      }
    }
    "pattern 2" in {
      import TEST302._
      inject[TestIF_302].provide shouldBe TestIFImpl_302_AUTO
      Try {
        inject[EX_TestIF_302].provide shouldBe TestIFImpl_302_AUTO
      } match {
        case scala.util.Failure(_: DIAutoInitializationException) => succeed
        case _ => fail()
      }

    }

    "type erase" in {
      import TEST303._

      val r_A = Wrap(new TestIF_303_A {})

      val r_B = Wrap(new TestIF_303_B {})

      overwrite[Wrap_303[TestIF_303_A]](r_A)
      overwrite[Wrap_303[TestIF_303_B]](r_B)

      import scala.reflect.runtime.universe._
      def get[T <: TestIF : WeakTypeTag]: Lazy[Wrap_303[T]] = inject[Wrap_303[T]]

      get[TestIF_303_A].provide shouldBe r_A
      get[TestIF_303_B].provide shouldBe r_B
    }

    "type erase with Seq" in {
      import TEST304._

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

      overwrite[Seq[TestIF_304_A]](r_A)
      overwrite[Seq[TestIF_304_B]](r_B)

      inject[Seq[TestIF_304_A]].provide shouldBe r_A
      inject[Seq[TestIF_304_B]].provide shouldBe r_B
    }

    "type erase with Auto" in {
      import TEST305._

      import scala.reflect.runtime.universe._
      def get[T <: TestIF : WeakTypeTag]: Lazy[Wrap_305[T]] = inject[Wrap_305[T]]

      get[TestIF_305_A].provide shouldBe TestIF_305_A_WRAP
      get[TestIF_305_B].provide shouldBe TestIF_305_B_WRAP
    }
  }
}
