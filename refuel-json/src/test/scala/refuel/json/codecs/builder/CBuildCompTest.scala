package refuel.json.codecs.builder

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{Codec, CodecDef, JsonTransform}

class CBuildCompTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {

  import refuel.json.model.CBuildCompTypeDef._

  private[this] val innerJson =
    s"""
       |{
       |  "1/4": {
       |    "1/3": "test1",
       |    "2/3": "test2"
       |  },
       |  "2/4": {
       |    "1/3": "test3",
       |    "2/3": "test4",
       |    "3/3": "test5"
       |  },
       |  "4/4": {
       |    "value": "test6"
       |  }
       |}
       |""".stripMargin

  private[this] val rawJson =
    s"""
       |{
       |  "root": $innerJson
       |}
       |""".stripMargin

  "single codec combine test" should {

    val CodecA = ConstCodec.from("1/3", "2/3", "3/3")(A.apply)(A.unapply)
    val CodecAA = CaseClassCodec.from[AA]

    implicit val CodecC: Codec[C] = "root".parsed(
      {
        "1/4".parsed(CodecA) ++
          "2/4".parsed(CodecA) ++
          "3/4".parsed(option(CodecAA)) ++
          "4/4".parsed(option(CodecAA))
        }.apply(B.apply)(B.unapply)
    ).apply(C.apply)(C.unapply)

    val casedValue = C(
      B(
        A("test1", "test2", None),
        A("test3", "test4", Some("test5")),
        None,
        Some(AA("test6"))
      )
    )


    "deserialize verification of complex codec build" in {
      rawJson.as[C] shouldBe Right(casedValue)
    }

    "serialize verification of complex codec build" in {
      casedValue.toJString.as[C] shouldBe Right(casedValue)
    }
  }

  "multi codec combine test" should {

    def buildLiteralCodecDep3A(pattern: Int): Codec[Depth3LineA] = pattern match {
      case 1 => ConstCodec.from("1/3")(Depth3LineA.apply)(Depth3LineA.unapply)
      case 2 => ConstCodec.from("2/3")(Depth3LineA.apply)(Depth3LineA.unapply)
      case 3 => ConstCodec.from("3/3")(Depth3LineA.apply)(Depth3LineA.unapply)
    }

    def buildLiteralCodecDep2A(pattern: Int): Codec[Depth2LineA] = pattern match {
      case 1 => "1/4".extend(
        {
          buildLiteralCodecDep3A(1) ++
            buildLiteralCodecDep3A(2) ++
            option(buildLiteralCodecDep3A(3))
        }.apply(Depth2LineA.apply)(Depth2LineA.unapply)
      )
      case 2 => "2/4".extend(
        {
          buildLiteralCodecDep3A(1) ++
            buildLiteralCodecDep3A(2) ++
            option(buildLiteralCodecDep3A(3))
        }.apply(Depth2LineA.apply)(Depth2LineA.unapply)
      )
    }

    def buildLiteralCodecDep2B(pattern: Int): Codec[Depth2LineB] = pattern match {
      case 1 => "3/4".parsed(
        CaseClassCodec.from[Depth3LineA]
      )(Depth2LineB.apply)(Depth2LineB.unapply)
      case 2 => "4/4".parsed(
        CaseClassCodec.from[Depth3LineA]
      )(Depth2LineB.apply)(Depth2LineB.unapply)
    }

    implicit val rootCodec: Codec[Depth1] = "root".extend(
      (buildLiteralCodecDep2A(1) ++
      buildLiteralCodecDep2A(2) ++
      option(buildLiteralCodecDep2B(1)) ++
      option(buildLiteralCodecDep2B(2))).apply(Depth1.apply)(Depth1.unapply)
    )

    "deserialize verification of complex codec build" in {
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

    "serialize verification of complex codec build" in {
      val x = Depth1(
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
      x.toJString.as[Depth1] shouldBe Right(x)
    }
  }

  "multi codec combine of root test" should {
    val CodecA = ConstCodec.from("1/3", "2/3", "3/3")(A.apply)(A.unapply)
    val CodecAA = CaseClassCodec.from[AA]

    val CodecB = {
      "1/4".parsed(CodecA) ++
        "2/4".parsed(CodecA) ++
        "3/4".parsed(option(CodecAA)) ++
        "4/4".parsed(option(CodecAA))
      }.apply(B.apply)(B.unapply)

    "deserialize verification of complex codec build" in {
      innerJson.as(CodecB) shouldBe Right(
        B(
          A("test1", "test2", None),
          A("test3", "test4", Some("test5")),
          None,
          Some(AA("test6"))
        )
      )
    }

    "serialize verification of complex codec build" in {
      val x = B(
        A("test1", "test2", None),
        A("test3", "test4", Some("test5")),
        None,
        Some(AA("test6"))
      )
      x.toJString(CodecB).as(CodecB) shouldBe Right(x)
    }
  }

  "Parse for each bounded block" should {
    val unpartitioningJson =
      s"""
         |{
         |  "root": {
         |    "test1": "test1#",
         |    "test2": "test2#",
         |    "test3": "test3#",
         |    "test4": "test4#",
         |    "test5": "test5#",
         |    "test6": "test6#"
         |  }
         |}
         |""".stripMargin

    val codec = "root".extend(
      {
        ConstCodec.from("test1", "test2", "test5", "test6")(String4.apply)(String4.unapply) ++
          option(ConstCodec.from("test3", "test4", "test7", "test8")(String4.apply)(String4.unapply))
      }.apply(String4_2.apply)(String4_2.unapply)
    )

    "deserialize verification of complex codec build" in {
      unpartitioningJson.as(codec) shouldBe Right(
        String4_2(
          String4("test1#", "test2#", "test5#", "test6#"),
          None
        )
      )
    }

    "serialize verification of complex codec build" in {
      val x = String4_2(
        String4("test1#", "test2#", "test5#", "test6#"),
        None
      )
      x.toJString(codec).as(codec) shouldBe Right(x)
    }
  }

  "Multi-level Parse for each bounded block" should {

    val unpartitioningJson =
      s"""
         |{
         |  "root": {
         |    "test1": "test1#",
         |    "test2": "test2#",
         |    "test3": "test3#",
         |    "test4": "test4#",
         |    "test5": "test5#",
         |    "test6": "test6#"
         |  }
         |}
         |""".stripMargin

    val codec: Codec[(String, String, String, Option[String])] = {
      ("root" / "test1").extend[String] ++
        ("root" / "test2").extend[String] ++
        ("root" / "test3").extend[String] ++
        option(("root" / "test8").extend[String])
    }.apply((a,b,c,d) => (a,b,c,d))(x => Some(x))

    "deserialize verification of complex codec build" in {
      unpartitioningJson.as(codec) shouldBe Right(
        ("test1#","test2#","test3#",None)
      )
    }

    "serialize verification of complex codec build" in {
      val x = ("test1#","test2#","test3#",None): (String, String, String, Option[String])
      x.toJString(codec).as(codec) shouldBe Right(x)
    }
  }
}
