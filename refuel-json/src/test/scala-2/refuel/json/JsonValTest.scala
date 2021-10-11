package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import JsonTransform._
import org.scalatest.Failed

class JsonValTest extends AsyncWordSpec with Matchers with Diagrams {
  "Json AST join" should {
    "obj + obj" in {
      val json = Json.obj("a" -> "aaa", "b" -> Json.arr("bbb")) ++ Json.obj("b" -> Json.arr("bbbb"), "c" -> "cccc")
      json.get shouldBe Json.obj("a" -> "aaa", "b" -> Json.arr("bbb", "bbbb"), "c" -> "cccc")
    }
    "duplicate literal in fail" in {
      val json = Json.obj("a" -> "aaa", "b" -> "bbb") ++ Json.obj("b" -> "bbbb", "c" -> "cccc")
      json.isFailure shouldBe true
    }

    "arr + arr" in {
      val json = Json.arr("aaa","bbb") ++ Json.arr("bbb", "ccc", "ddd")
      json.get shouldBe Json.arr("aaa","bbb", "bbb", "ccc", "ddd")
    }
  }
}
