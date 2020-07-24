package refuel.json.codecs.builder.context

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{CodecDef, Json, JsonTransform}

class DynamicCodecGenFeatureTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "Adding a pure key" should {
    "read" in {
      ReadWith[String]("aaa" @@ "bbb").deserialize(
        Json.obj(
          "aaa" -> Json.obj(
            "bbb" -> "ccc"
          )
        )
      ) shouldBe "ccc"
    }
    "write" in {
      WriteWith[String]("aaa" @@ "bbb").serialize("ccc") shouldBe Json.obj(
        "aaa" -> Json.obj(
          "bbb" -> "ccc"
        )
      )
    }
    "codec read" in {
      BothWith[String]("aaa" @@ "bbb").deserialize(
        Json.obj(
          "aaa" -> Json.obj(
            "bbb" -> "ccc"
          )
        )
      ) shouldBe "ccc"
    }
    "codec write" in {
      BothWith[String]("aaa" @@ "bbb").serialize("ccc") shouldBe Json.obj(
        "aaa" -> Json.obj(
          "bbb" -> "ccc"
        )
      )
    }
  }
}
