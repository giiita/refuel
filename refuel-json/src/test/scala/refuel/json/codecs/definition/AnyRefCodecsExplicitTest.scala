package refuel.json.codecs.definition

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.codecs.{Read, Write}
import refuel.json.error.UnsupportedOperation
import refuel.json.{Codec, CodecDef, Json, JsonTransform}

class AnyRefCodecsExplicitTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "Any ref codec auto generation" should {
    "Expected to be compileable without explicit Codec[_] generation" in {
      implicitly[Codec[Map[String, Option[List[Seq[Vector[Array[Set[(String, String)]]]]]]]]]
      succeed
    }

    "read auto inferred before deserialize" in {
      seqR(implicitly[Read[Seq[String]]]).deserialize(
        Json.arr(
          Json.arr("foo", "bar")
        )
      ) shouldBe Seq(Seq("foo", "bar"))
    }

    "write auto inferred before serialize" in {
      seq(implicitly[Write[Seq[String]]]).serialize(Seq(Seq("foo", "bar"))) shouldBe Json.arr(
        Json.arr("foo", "bar")
      )
    }
  }
}
