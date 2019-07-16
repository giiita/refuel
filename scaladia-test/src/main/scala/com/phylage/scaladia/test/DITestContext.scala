package com.phylage.scaladia.test

import com.phylage.scaladia.injector.RefreshInjection
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

trait DITestContext[T] extends AsyncWordSpec with Matchers with DiagrammedAssertions with RefreshInjection {
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
    * @param functionName
    * @tparam T
    */
  def test(functionName: String)
}
