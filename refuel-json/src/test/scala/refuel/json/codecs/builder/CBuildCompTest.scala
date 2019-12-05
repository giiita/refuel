package refuel.json.codecs.builder

import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}
import refuel.json.JsContext

import scala.util.{Failure, Success, Try}

object CBuildCompTest {
  case class A(test1: String, test2: String, test3: Option[String])
  case class AA(value: String)
  case class B(a1: A, a2: A, aa1: Option[AA], aa2: Option[AA])
  case class C(a: B)
}

class CBuildCompTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JsContext {
  import CBuildCompTest._
  "custom codec builder from codec" should {

    val CodecA = ConstCodec.from("1/3", "2/3", "3/3")(A.apply)(A.unapply)
    val CodecAA = CaseClassCodec.from[AA]

    val CodecC = "root".parsed(
      {
        "1/4".parsed(CodecA) ++
          "2/4".parsed(CodecA) ++
          "3/4".parsed(option(CodecAA)) ++
          "4/4".parsed(option(CodecAA))
        }.apply(B.apply)(B.unapply)
    ).apply(C.apply)(C.unapply)

    val rawJson = s"""
                     |{
                     |  "root": {
                     |    "1/4": {
                     |      "1/3": "test1",
                     |      "2/3": "test2"
                     |    },
                     |    "2/4": {
                     |      "1/3": "test3",
                     |      "2/3": "test4",
                     |      "3/3": "test5"
                     |    },
                     |    "4/4": {
                     |      "value": "test6"
                     |    }
                     |  }
                     |}
                     |""".stripMargin

    val casedValue = C(
      B(
        A("test1", "test2", None),
        A("test3", "test4", Some("test5")),
        None,
        Some(AA("test6"))
      )
    )


    "deserialize verification of complex codec build" in {
      Try {
        rawJson.jsonTree.named("root").named("1/4")
      } match {
        case Success(r) => println(s"TESTTEST : $r")
        case Failure(e) => e.printStackTrace()
      }
      rawJson.as(CodecC) shouldBe Right(casedValue)
    }
    "serialize verification of complex codec build" in {
      casedValue.toJson(CodecC).toString shouldBe {
        rawJson.replaceAll("\n", "")
          .replaceAll("\\s", "")
      }
    }
  }
}
