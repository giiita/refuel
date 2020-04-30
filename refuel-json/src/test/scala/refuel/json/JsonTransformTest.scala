package refuel.json

import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import refuel.json.error.IllegalJsonFormat
import refuel.json.model.TestJson.JString

class JsonTransformTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "Json tree build" should {
    "fail case - EOF position" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":123"""".jsonTree
      }.getMessage shouldBe s"""Unexpected EOF: {"value":123""""
    }
    "fail case - Unexpected final json tree" in {
      intercept[IllegalJsonFormat] {
        s"""{"value": }""".jsonTree
      }.getMessage shouldBe s"""Unspecified json value of key: #value#"""
    }
    "fail case - Syntax error" in {
      intercept[IllegalJsonFormat] {
        s"""{"value: "aaa"}""".jsonTree
      }.getMessage shouldBe s"""Unspecified json value of key: #value: #"""
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
          e.getMessage shouldBe s"""Internal structure analysis raised an exception."""
          e.getCause.getMessage shouldBe s"""Cannot deserialize to String -> null"""
        case Right(r) => fail(r.toString)
      }
    }
  }
}
