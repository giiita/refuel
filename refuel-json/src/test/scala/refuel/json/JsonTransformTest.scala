package refuel.json

import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import refuel.json.entry.JsEmpty
import refuel.json.error.IllegalJsonFormat
import refuel.json.model.TestJson.JString

class JsonTransformTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "Json tree build" should {
    "Empty input" in {
      s"".jsonTree shouldBe JsEmpty
    }
    "Whitespace only" in {
      s" ".jsonTree shouldBe JsEmpty
    }
    "Unicode type" in {
      s"""{"value":"test\uD83C\uDF0Ftest\uD83C\uDF0Ftest"}""".jsonTree shouldBe Json.obj(
        "value" -> s"""test🌏test🌏test"""
      )
    }
    "Backslash" in {
      s"""{"value":"xxx\\xxx"}""".jsonTree shouldBe Json.obj(
        "value" -> "xxx\\xxx"
      )
      Json
        .obj(
          "value" -> "xxx\\xxx"
        )
        .toJString shouldBe s"""{"value":"xxx\\\\xxx"}"""
    }
    "Breakline raw" in {
      s"""{"value":"foo\nbar"}""".jsonTree shouldBe Json.obj(
        "value" -> "foo\nbar"
      )
      s"""{"value":"foo\nbar"}""".as[String] shouldBe Right(s"""{"value":"foo\nbar"}""")
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
        "value" -> "foo\\nbar"
      )
      Json
        .obj(
          "value" -> "foo\nbar"
        )
        .des[String] shouldBe s"""{"value":"foo\nbar"}"""
    }
    "fail case - EOF position" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":123"""".jsonTree
      }.getMessage shouldBe s"""Unexpected EOF: {"value":123""""
    }
    "fail case - Unexpected final json tree" in {
      intercept[IllegalJsonFormat] {
        s"""{"value": }""".jsonTree
      }.getMessage shouldBe s"""Unspecified json value of key: #"value"#"""
    }
    "fail case - Syntax error" in {
      intercept[IllegalJsonFormat] {
        s"""{"value: "aaa"}""".jsonTree
      }.getMessage shouldBe s"""Unspecified json value of key: #"value:"#"""
    }
    "fail case - EOF position 2" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":"3}""".jsonTree
      }.getMessage shouldBe s"""Unexpected EOF: {"value":"3}"""
    }
    "fail case - Not found key name" in {
      s"""{"value":"3"}""".as(ConstCodec.from("hoge")(JString.apply)(JString.unapply)) match {
        case Left(e) =>
          e.printStackTrace()
          e.getMessage shouldBe """Internal structure analysis by class refuel.json.codecs.JoinableCodec$T1 raised an exception."""
          e.getCause.getMessage shouldBe s"""Cannot deserialize null into a String"""
        case Right(r) => fail(r.toString)
      }
    }
  }
}
