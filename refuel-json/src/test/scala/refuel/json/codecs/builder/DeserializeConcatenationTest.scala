package refuel.json.codecs.builder

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.error.UnexpectedDeserializeType
import refuel.json.{CodecDef, Json, JsonTransform}

object DeserializeConcatenationTest {}
class DeserializeConcatenationTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "pipe function" should {
    "Reads result controll success case." in {
      ReadWith[String]("status")
        .readMap {
          case "success" => ReadWith[String]("body")
          case "failure" => ReadWith[String]("error")
        }
        .deserialize(
          Json.obj(
            "status" -> "success",
            "body"   -> "test1",
            "error"  -> "test2"
          )
        ) shouldBe "test1"
    }
    "Reads result controll failure case." in {
      ReadWith[String]("status")
        .readMap {
          case "success" => ReadWith[String]("body")
          case "failure" => ReadWith[String]("error")
        }
        .deserialize(
          Json.obj(
            "status" -> "failure",
            "body"   -> "test1",
            "error"  -> "test2"
          )
        ) shouldBe "test2"
    }
    "Reads result controll unexpected case." in {
      intercept[UnexpectedDeserializeType] {
        ReadWith[String]("status")
          .readMap {
            case "success" => ReadWith[String]("body")
            case "failure" => ReadWith[String]("error")
          }
          .deserialize(
            Json.obj(
              "body"  -> "test1",
              "error" -> "test2"
            )
          )
      }.getMessage shouldBe "Cannot deserialize null into a String"
    }
  }
}
