package refuel.json

import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}
import refuel.json.codecs.factory.ConstCodec
import refuel.json.error.IllegalJsonFormat
import refuel.json.model.TestJson.JString

class JsContextTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JsContext {
  "Json tree build" should {
    "fail case - EOF position" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":123"""".jsonTree
      }.getMessage shouldBe "EOF in an unexpected position."
    }
    "fail case - Unexpected final json tree" in {
      intercept[IllegalJsonFormat] {
        s"""{"value": }""".jsonTree
      }.getMessage shouldBe "The conversion was successful, but the generated JsonTree is invalid.\n{\"value\": }"
    }
//    "fail case - Syntax error" in {
//      val a = s"""{"value: "aaa"}""".jsonTree
//
//      val r = intercept[IllegalJsonFormat] {
//        s"""{"value: "aaa"}""".jsonTree
//      }.getMessage
//      println(r)
//      r shouldBe "The conversion was successful, but the generated JsonTree is invalid.\n{\"value\": }"
//    }
    "fail case - EOF position 2" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":"3}""".jsonTree
      }.getMessage shouldBe "EOF in an unexpected position.\n{\"value\":\"<ERROR FROM>3}"
    }
    "fail case - Not found key name" in {
      s"""{"value":"3"}""".as(ConstCodec.from("hoge")(JString.apply)(JString.unapply)) match {
        case Left(e) =>
          e.getMessage shouldBe "Cannot deserialize to String -> null"
        case Right(r)       => fail(r.toString)
      }
    }
  }
}