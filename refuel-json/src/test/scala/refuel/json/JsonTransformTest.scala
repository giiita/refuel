package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.entry.JsEmpty
import refuel.json.error.{IllegalJsonFormat, StreamIndeterminate}
import refuel.json.model.TestJson.JString

object JsonTransformTest extends CodecDef {
  case class ProjectName(projectName: String)
  val ProjectReads = CaseClassCodec.from[ProjectName]
}
class JsonTransformTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "Json tree build" should {
    "Empty input" in {
      s"".jsonTree shouldBe JsEmpty
    }
    "Whitespace only" in {
      s" ".jsonTree shouldBe JsEmpty
    }

    "Unicode type" in {
      s"""{"title":"${(1 to 1 << 8).map(_ => "\\n").mkString}"}""".jsonTree shouldBe Json.obj(
        "title" -> (1 to 1 << 8).map(_ => "\n").mkString
      )
      s"""{"value":"test\uD83C\uDF0Ftest\uD83C\uDF0Ftest"}""".jsonTree shouldBe Json.obj(
        "value" -> s"""test🌏test🌏test"""
      )
      Json.obj("value" -> s"""test🌏test🌏test""").encodedStr shouldBe s"""{"value":"test🌏test🌏test"}"""
      Json.obj("value" -> s"""test🌏test🌏test""").toString shouldBe s"""{"value":"test🌏test🌏test"}"""
      Json.obj("value" -> s"""test🌏test🌏test""").des[String] shouldBe s"""{"value":"test🌏test🌏test"}"""
    }
    "Unicode type with escaped" in {
      s"""{"value":"test\\uD83C\\uDF0Ftest\\uD83C\\uDF0Ftest"}""".jsonTree shouldBe Json.obj(
        "value" -> s"""test🌏test🌏test"""
      )
    }

    "Backslash" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":"xxx\\xxx"}""".jsonTree
      }.getMessage shouldBe s"Illegal json format: \\x"

      s"""{"value":"xxx\\\\xxx"}""".jsonTree shouldBe Json.obj(
        "value" -> "xxx\\xxx"
      )
      Json.obj("value" -> "xxx\\xxx").named("value").encodedStr shouldBe s""""xxx\\\\xxx""""
      Json.obj("value" -> "xxx\\xxx").named("value").toString shouldBe s""""xxx\\\\xxx""""
      Json.obj("value" -> "xxx\\xxx").named("value").des[String] shouldBe "xxx\\xxx"
      Json.obj("value" -> "xxx\\xxx").encodedStr shouldBe s"""{"value":"xxx\\\\xxx"}"""
      Json.obj("value" -> "xxx\\xxx").toString shouldBe s"""{"value":"xxx\\\\xxx"}"""
      Json.obj("value" -> "xxx\\xxx").des[String] shouldBe s"""{"value":"xxx\\\\xxx"}"""
    }
    "quotation" in {
      intercept[StreamIndeterminate] {
        s"""{"value":"xxx"xxx"}""".jsonTree
      }.getMessage shouldBe "Cannot add JsKey to JsObject. Must be JsString, but was xxx\""

      intercept[StreamIndeterminate] {
        s"""{"value":"foo\\\\"bar"}""".jsonTree
      }.getMessage shouldBe "Cannot add JsKey to JsObject. Must be JsString, but was bar\""

      s"""{"value":"foo\\"bar"}""".jsonTree shouldBe Json.obj("value" -> "foo\"bar")

      s"""{"value":"foo\\"bar"}""".jsonTree.named("value") shouldBe Json.str("foo\"bar")

      s"""{"value":"foo\\"bar"}""".as[String] shouldBe Right(s"""{"value":"foo\\"bar"}""")

      s"""{"value":"foo\\"bar"}""".jsonTree.encodedStr shouldBe s"""{"value":"foo\\"bar"}"""

      s"""{"value":"foo\\"bar"}""".jsonTree.toString shouldBe s"""{"value":"foo\\"bar"}"""

      s"""{"value":"foo\\"bar"}""".jsonTree.des[String] shouldBe s"""{"value":"foo\\"bar"}"""

      s"""{"value":"foo\\/bar"}""".jsonTree shouldBe Json.obj("value" -> "foo/bar")
    }
    "Breakline raw" in {
      s"""{"value":"foo\nbar"}""".jsonTree shouldBe Json.obj("value"  -> "foo\nbar")
      s"""{"value":"foo\\nbar"}""".jsonTree shouldBe Json.obj("value" -> "foo\nbar")

      s"""{"value":"foo\nbar"}""".as[String] shouldBe Right(s"""{"value":"foo\\nbar"}""")
      s"""{"value":"foo\\nbar"}""".as[String] shouldBe Right(s"""{"value":"foo\\nbar"}""")

      s"""{"value":"foo\nbar"}""".jsonTree.encodedStr shouldBe s"""{"value":"foo\\nbar"}"""
      s"""{"value":"foo\\nbar"}""".jsonTree.encodedStr shouldBe s"""{"value":"foo\\nbar"}"""

      s"""{"value":"foo\nbar"}""".jsonTree.toString shouldBe s"""{"value":"foo\\nbar"}"""
      s"""{"value":"foo\\nbar"}""".jsonTree.toString shouldBe s"""{"value":"foo\\nbar"}"""

      s"""{"value":"foo\nbar"}""".jsonTree.des[String] shouldBe s"""{"value":"foo\\nbar"}"""
      s"""{"value":"foo\\nbar"}""".jsonTree.des[String] shouldBe s"""{"value":"foo\\nbar"}"""
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
      }.getMessage shouldBe s"""Unspecified json value of key: #"value: "#"""
    }
    "fail case - EOF position 2" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":"3}""".jsonTree
      }.getMessage shouldBe s"""Unexpected EOF: {"value":"3}"""
    }
    "fail case - Not found key name" in {
      s"""{"value":"3"}""".as(ConstCodec.from("hoge")(JString.apply)(JString.unapply)) match {
        case Left(e) =>
          e.getMessage shouldBe """Internal structure analysis by class refuel.json.codecs.JoinableCodec$T1 raised an exception."""
          e.getCause.getMessage shouldBe s"""Cannot deserialize null into a String"""
        case Right(r) => fail(r.toString)
      }
    }
    "many array" in {
      import JsonTransformTest._
      val b =
        """{"projectName":"[\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\"]"}"""
      println(b.jsonTree.des(ProjectReads))
      succeed
    }

    "Nested array tree" in {
      val json = s"""
         |{
         |  "blocks": [
         |    {
         |      "type": "rich_text",
         |      "block_id": "vMWQ",
         |      "elements": [
         |        {
         |          "type": "rich_text_section",
         |          "elements": [
         |            {
         |              "type": "user",
         |              "user_id": "U01BBCSP6NP"
         |            },
         |            {
         |              "type": "text",
         |              "text": "hey"
         |            }
         |          ]
         |        }
         |      ]
         |    }
         |  ]
         |}
         |""".stripMargin.jsonTree
      json.named("blocks").named("elements").named("elements").named("text") shouldBe Json.arr(Json.str("hey"))
      json.named("blocks" @@ "elements" @@ "elements" @@ "text") shouldBe Json.arr(Json.str("hey"))
    }
  }
}
