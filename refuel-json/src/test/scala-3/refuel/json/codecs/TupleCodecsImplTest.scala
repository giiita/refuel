package refuel.json.codecs

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{Json, JsonTransform}

class TupleCodecsImplTest extends AsyncWordSpec with Matchers with Diagrams {
  import JsonTransform.{*, given}
  "Implicitlt tuple conversion" should {
    "tuple2" in {
      val value = "a" -> "b"
      val tree = Json.arr("a", "b")
      tree.readAs[(String, String)].fold(fail(_), _ shouldBe value)
      value.writeAs[(String, String)].fold(fail(_), _ shouldBe tree)
    }
    "tuple3" in {
      val value = ("a", "b", 1)
      val tree = Json.arr("a", "b", 1)
      tree.readAs[(String, String, Int)].fold(fail(_), _ shouldBe value)
      value.writeAs[(String, String, Int)].fold(fail(_), _ shouldBe tree)
    }
    "tuple22" in {
      val value = ("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "k", "l")
      val tree = Json.arr("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "k", "l")
      tree.readAs[(String, String, String, String, String, String, String, String, String, String, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String)].fold(fail(_), _ shouldBe value)
      value.writeAs[(String, String, String, String, String, String, String, String, String, String, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, String, String)].fold(fail(_), _ shouldBe tree)
    }
    "tuple30 in manual" in {
      val value = ("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "k", "l", "m", "n", "o", "p", "q")
      val tree = Json.arr("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "k", "l", "m", "n", "o", "p", "q")
      tree.readAs(using ReadOf.tuple[String *: String *: String *: String *: String *: String *: String *: String *: String *: String *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: String *: String *: String *: String *: String *: String *: String *: EmptyTuple]).fold(fail(_), _ shouldBe value)
      value.writeAs(using WriteOf.tuple[String *: String *: String *: String *: String *: String *: String *: String *: String *: String *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: Int *: String *: String *: String *: String *: String *: String *: String *: EmptyTuple]).fold(fail(_), _ shouldBe tree)
    }
  }
}
