package com.phylage.scaladia.test

import com.phylage.scaladia.injector.Injector
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

trait ScaladiaTest[T] extends AsyncWordSpec with Matchers with DiagrammedAssertions with Injector {
  /**
    * {{{
    *   class HogeTest extends DITestContext[Hoge] {
    *     function(_.getValue) {
    *       input(xxx).then { result =>
    *         result shouldBe "pattern A"
    *       }
    *
    *       input(yyy).then { result =>
    *         result shouldBe "pattern B"
    *       }
    *     }
    *   }
    * }}}
    *
    * @param functionName
    * @tparam T
    */
  def test(functionName: String)

  new Sha[Xxx] { Xxx =>
    test(Xxx.)
  }

  class Sha[X]() {
    def test(ll: X => String => Int): Unit = {}
  }

  class Xxx {
    def xxx(x: String): Int = 0
  }
}
