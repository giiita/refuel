package refuel.json.codecs.builder

import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}
import refuel.json.{Codec, JsContext}

class CBuildCompTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JsContext {

  import refuel.json.model.CBuildCompTypeDef._

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

    val rawJson =
      s"""
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
      rawJson.as(CodecC) shouldBe Right(casedValue)
    }

    "serialize verification of complex codec build" in {
      casedValue.toJson(CodecC).toString shouldBe {
        rawJson.replaceAll("\n", "")
          .replaceAll("\\s", "")
      }
    }

    "multi-line codec deserialization" in {

      def buildLiteralCodecDep3A(pattern: Int): Codec[Depth3LineA] = pattern match {
        case 1 => ConstCodec.from("1/3")(Depth3LineA.apply)(Depth3LineA.unapply)
        case 2 => ConstCodec.from("2/3")(Depth3LineA.apply)(Depth3LineA.unapply)
        case 3 => ConstCodec.from("3/3")(Depth3LineA.apply)(Depth3LineA.unapply)
      }

      def buildLiteralCodecDep2A(pattern: Int): Codec[Depth2LineA] = pattern match {
        case 1 => "1/4".parsed(
          buildLiteralCodecDep3A(1),
          buildLiteralCodecDep3A(2),
          option(buildLiteralCodecDep3A(3))
        )(Depth2LineA.apply)(Depth2LineA.unapply)
        case 2 => "2/4".parsed(
          buildLiteralCodecDep3A(1),
          buildLiteralCodecDep3A(2),
          option(buildLiteralCodecDep3A(3))
        )(Depth2LineA.apply)(Depth2LineA.unapply)
      }

      def buildLiteralCodecDep2B(pattern: Int): Codec[Depth2LineB] = pattern match {
        case 1 => "3/4".parsed(
          CaseClassCodec.from[Depth3LineA]
        )(Depth2LineB.apply)(Depth2LineB.unapply)
        case 2 => "3/4".parsed(
          CaseClassCodec.from[Depth3LineA]
        )(Depth2LineB.apply)(Depth2LineB.unapply)
      }

      val rootCodec = "root".parsed(
        buildLiteralCodecDep2A(1),
        buildLiteralCodecDep2A(2),
        option(buildLiteralCodecDep2B(1)),
        option(buildLiteralCodecDep2B(2))
      )(Depth1.apply)(Depth1.unapply)

      val x = rawJson.jsonTree

      rawJson.as(rootCodec) match {
        case Left(e) => e.printStackTrace()
        case _ =>
      }

      rawJson.as(rootCodec) shouldBe Right(
        Depth1(
          Depth2LineA(
            Depth3LineA("test1"),
            Depth3LineA("test2"),
            None
          ),
          Depth2LineA(
            Depth3LineA("test3"),
            Depth3LineA("test4"),
            Some(Depth3LineA("test5"))
          ),
          None,
          Some(
            Depth2LineB(Depth3LineA("test6"))
          )
        )
      )
    }
  }
}
