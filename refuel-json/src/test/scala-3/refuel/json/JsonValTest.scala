package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class JsonValTest extends AsyncWordSpec with Matchers with Diagrams {
  "Json AST join" should {
    "obj + obj" in {
      val json = Json.obj("a" -> "aaa", "b" -> "bbb") ++ Json.obj("b" -> "bbbb", "c" -> "cccc")
      json shouldBe Json.obj("a" -> "aaa", "b" -> "bbbb", "c" -> "cccc")
    }
  }
}
