package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class EncodedJsonTransformTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with EncodedJsonTranform
    with CodecDef {
  "Json tree build" should {
    "Unicode type" in {
      s"""{"value":"test\uD83C\uDF0Ftest\uD83C\uDF0Ftest"}""".jsonTree shouldBe Json.obj(
        "value" -> s"""test🌏test🌏test"""
      )
    }
    "Breakline raw" in {
      s"""{"value":"foo\nbar"}""".jsonTree shouldBe Json.obj(
        "value" -> "foo\nbar"
      )
      s"""{"value":"foo\\nbar"}""".jsonTree shouldBe Json.obj(
        "value" -> "foo\nbar"
      )
      s"""{"value":"foo\\\\nbar"}""".jsonTree shouldBe Json.obj(
        "value" -> "foo\nbar"
      )
      s"""{"value":"foo\\\\nbar"}""".as[String] shouldBe Right(s"""{"value":"foo\nbar"}""")
      Json
        .obj(
          "value" -> "foo\nbar"
        )
        .toJString shouldBe s"""{"value":"foo\\nbar"}"""
      Json
        .obj(
          "value" -> "foo\nbar"
        )
        .toString shouldBe s"""{"value":"foo\nbar"}"""
    }
    "Breakline" in {
      s"""{"value":"foo\\nbar"}""".jsonTree shouldBe Json.obj(
        "value" -> "foo\nbar"
      )
      Json
        .obj(
          "value" -> "foo\nbar"
        )
        .des[String] shouldBe s"""{"value":"foo\nbar"}"""
    }
  }
}
