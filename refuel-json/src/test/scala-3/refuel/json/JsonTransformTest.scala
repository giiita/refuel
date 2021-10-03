package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.JsonVal.JsEmpty
import refuel.json.codecs.Read
import refuel.json.exception.{IllegalJsonFormatException, UnexpectedJsonTreeException}
import JsonTransform.{*, given}

import scala.util.{Failure, Success}

object JsonTransformTest {
  val ProjectReads = CaseClass[ProjectName]

  case class ProjectName(projectName: String)
}
class JsonTransformTest extends AsyncWordSpec with Matchers with Diagrams {
  import scala.language.implicitConversions
  "Json tree build" should {
    "Empty input" in {
      "".readAs[String]
      s"".json shouldBe JsEmpty
    }
    "Whitespace only" in {
      s" ".json shouldBe JsEmpty
    }

    "Unicode type" in {
      s"""{"title":"${(1 to 1 << 8).map(_ => "\\n").mkString}"}""".json shouldBe Json.obj(
        "title" -> (1 to 1 << 8).map(_ => "\n").mkString
      )
      s"""{"value":"test\uD83C\uDF0Ftest\uD83C\uDF0Ftest"}""".json shouldBe Json.obj(
        "value" -> s"""test🌏test🌏test"""
      )
      Json.obj("value" -> s"""test🌏test🌏test""").toString shouldBe s"""{"value":"test🌏test🌏test"}"""
      Json.obj("value" -> s"""test🌏test🌏test""").toString shouldBe s"""{"value":"test🌏test🌏test"}"""
      Json.obj("value" -> s"""test🌏test🌏test""").readAs[String] shouldBe Success(s"""{"value":"test🌏test🌏test"}""")
    }
    "Unicode type with escaped" in {
      s"""{"value":"test\\uD83C\\uDF0Ftest\\uD83C\\uDF0Ftest"}""".json shouldBe Json.obj(
        "value" -> s"""test🌏test🌏test"""
      )
    }

    "Backslash" in {
      intercept[IllegalJsonFormatException] {
        s"""{"value":"xxx\\xxx"}""".json
      }.getMessage shouldBe s"Illegal json format: \\x"

      s"""{"value":"xxx\\\\xxx"}""".json shouldBe Json.obj(
        "value" -> "xxx\\xxx"
      )
      Json.obj("value" -> "xxx\\xxx").named("value").toString shouldBe s""""xxx\\\\xxx""""
      Json.obj("value" -> "xxx\\xxx").named("value").readAs[String] shouldBe Success("xxx\\xxx")
      Json.obj("value" -> "xxx\\xxx").toString shouldBe s"""{"value":"xxx\\\\xxx"}"""
      Json.obj("value" -> "xxx\\xxx").readAs[String] shouldBe Success(s"""{"value":"xxx\\\\xxx"}""")
    }
    "quotation" in {
      intercept[UnexpectedJsonTreeException] {
        s"""{"value":"xxx"xxx"}""".json
      }.getMessage shouldBe "Cannot add JsKey to JsObject. Must be JsString, but was xxx\""

      intercept[UnexpectedJsonTreeException] {
        s"""{"value":"foo\\\\"bar"}""".json
      }.getMessage shouldBe "Cannot add JsKey to JsObject. Must be JsString, but was bar\""

      s"""{"value":"foo\\"bar"}""".json shouldBe Json.obj("value" -> "foo\"bar")

      s"""{"value":"foo\\"bar"}""".json.named("value") shouldBe Json.str("foo\"bar")

      s"""{"value":"foo\\"bar"}""".readAs[String] shouldBe Success(s"""{"value":"foo\\"bar"}""")

      s"""{"value":"foo\\"bar"}""".json.toString shouldBe s"""{"value":"foo\\"bar"}"""

      s"""{"value":"foo\\"bar"}""".json.readAs[String] shouldBe Success(s"""{"value":"foo\\"bar"}""")

      s"""{"value":"foo\\/bar"}""".json shouldBe Json.obj("value" -> "foo/bar")
    }
    "Breakline raw" in {
      s"""{"value":"foo\nbar"}""".json shouldBe Json.obj("value"  -> "foo\nbar")
      s"""{"value":"foo\\nbar"}""".json shouldBe Json.obj("value" -> "foo\nbar")

      s"""{"value":"foo\nbar"}""".readAs[String] shouldBe Success(s"""{"value":"foo\\nbar"}""")
      s"""{"value":"foo\\nbar"}""".readAs[String] shouldBe Success(s"""{"value":"foo\\nbar"}""")

      s"""{"value":"foo\nbar"}""".json.toString shouldBe s"""{"value":"foo\\nbar"}"""
      s"""{"value":"foo\\nbar"}""".json.toString shouldBe s"""{"value":"foo\\nbar"}"""

      s"""{"value":"foo\nbar"}""".json.readAs[String] shouldBe Success(s"""{"value":"foo\\nbar"}""")
      s"""{"value":"foo\\nbar"}""".json.readAs[String] shouldBe Success(s"""{"value":"foo\\nbar"}""")
    }
    "fail case - EOF position" in {
      intercept[IllegalJsonFormatException] {
        s"""{"value":123"""".json
      }.getMessage shouldBe s"""Unexpected EOF: {"value":123""""
    }
    "fail case - Unexpected final json tree" in {
      intercept[IllegalJsonFormatException] {
        s"""{"value": }""".json
      }.getMessage shouldBe s"""Unspecified json value of key: #"value"#"""
    }
    "fail case - Syntax error" in {
      intercept[IllegalJsonFormatException] {
        s"""{"value: "aaa"}""".json
      }.getMessage shouldBe s"""Unspecified json value of key: #"value: "#"""
    }
    "fail case - EOF position 2" in {
      intercept[IllegalJsonFormatException] {
        s"""{"value":"3}""".json
      }.getMessage shouldBe s"""Unexpected EOF: {"value":"3}"""
    }
    "many array" in {
      import JsonTransformTest.*
      val b =
        """{"projectName":"[\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\",\"SC_210310_25\"]"}"""
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
         |""".stripMargin.json
      json.named("blocks").named("elements").named("elements").named("text") shouldBe Json.arr(Json.str("hey"))
      json.named("blocks" @@ "elements" @@ "elements" @@ "text") shouldBe Json.arr(Json.str("hey"))
    }
  }
}
