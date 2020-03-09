package refuel.json.codecs.definition

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{Codec, CodecDef, JsonTransform}
import refuel.json.model.TestJson
import refuel.json.model.TestJson._

class TupleCodecsImplTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {

  import scala.language.implicitConversions
  implicit val JTuple2Codec: Codec[JTuple2] = CaseClassCodec.from[JTuple2]
  implicit val JTuple3Codec: Codec[JTuple3] = CaseClassCodec.from[JTuple3]
  implicit val JTuple4Codec: Codec[JTuple4] = CaseClassCodec.from[JTuple4]
  implicit val JTuple5Codec: Codec[JTuple5] = CaseClassCodec.from[JTuple5]
  implicit val JTuple6Codec: Codec[JTuple6] = CaseClassCodec.from[JTuple6]
  implicit val JTuple7Codec: Codec[JTuple7] = CaseClassCodec.from[JTuple7]
  implicit val JTuple8Codec: Codec[JTuple8] = CaseClassCodec.from[JTuple8]
  implicit val JTuple9Codec: Codec[JTuple9] = CaseClassCodec.from[JTuple9]
  implicit val JTuple10Codec: Codec[JTuple10] = CaseClassCodec.from[JTuple10]
  implicit val JTuple11Codec: Codec[JTuple11] = CaseClassCodec.from[JTuple11]
  implicit val JTuple12Codec: Codec[JTuple12] = CaseClassCodec.from[JTuple12]
  implicit val JTuple13Codec: Codec[JTuple13] = CaseClassCodec.from[JTuple13]
  implicit val JTuple14Codec: Codec[JTuple14] = CaseClassCodec.from[JTuple14]
  implicit val JTuple15Codec: Codec[JTuple15] = CaseClassCodec.from[JTuple15]
  implicit val JTuple16Codec: Codec[JTuple16] = CaseClassCodec.from[JTuple16]
  implicit val JTuple17Codec: Codec[JTuple17] = CaseClassCodec.from[JTuple17]
  implicit val JTuple18Codec: Codec[JTuple18] = CaseClassCodec.from[JTuple18]
  implicit val JTuple19Codec: Codec[JTuple19] = CaseClassCodec.from[JTuple19]
  implicit val JTuple20Codec: Codec[JTuple20] = CaseClassCodec.from[JTuple20]
  implicit val JTuple21Codec: Codec[JTuple21] = CaseClassCodec.from[JTuple21]
  implicit val JTuple22Codec: Codec[JTuple22] = CaseClassCodec.from[JTuple22]

  "json deserialize" should {
    "Tuple2 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 2).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple2Codec) shouldBe Right {
        TestJson.JTuple2((1, 2))
      }
    }
    "Tuple3 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 3).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple3Codec) shouldBe Right {
        TestJson.JTuple3((1, 2, 3))
      }
    }
    "Tuple4 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 4).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple4Codec) shouldBe Right {
        TestJson.JTuple4((1, 2, 3, 4))
      }
    }
    "Tuple5 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 5).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple5Codec) shouldBe Right {
        TestJson.JTuple5((1, 2, 3, 4, 5))
      }
    }
    "Tuple6 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 6).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple6Codec) shouldBe Right {
        TestJson.JTuple6((1, 2, 3, 4, 5, 6))
      }
    }
    "Tuple7 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 7).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple7Codec) shouldBe Right {
        TestJson.JTuple7((1, 2, 3, 4, 5, 6, 7))
      }
    }
    "Tuple8 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 8).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple8Codec) shouldBe Right {
        TestJson.JTuple8((1, 2, 3, 4, 5, 6, 7, 8))
      }
    }
    "Tuple9 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 9).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple9Codec) shouldBe Right {
        TestJson.JTuple9((1, 2, 3, 4, 5, 6, 7, 8, 9))
      }
    }
    "Tuple10 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 10).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple10Codec) shouldBe Right {
        TestJson.JTuple10((1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
      }
    }
    "Tuple11 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 11).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple11Codec) shouldBe Right {
        TestJson.JTuple11((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
      }
    }
    "Tuple12 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 12).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple12Codec) shouldBe Right {
        TestJson.JTuple12((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
      }
    }
    "Tuple13 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 13).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple13Codec) shouldBe Right {
        TestJson.JTuple13((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13))
      }
    }
    "Tuple14 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 14).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple14Codec) shouldBe Right {
        TestJson.JTuple14((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14))
      }
    }
    "Tuple15 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 15).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple15Codec) shouldBe Right {
        TestJson.JTuple15((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
      }
    }
    "Tuple16 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 16).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple16Codec) shouldBe Right {
        TestJson.JTuple16((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16))
      }
    }
    "Tuple17 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 17).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple17Codec) shouldBe Right {
        TestJson.JTuple17((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17))
      }
    }
    "Tuple18 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 18).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple18Codec) shouldBe Right {
        TestJson.JTuple18((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18))
      }
    }
    "Tuple19 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 19).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple19Codec) shouldBe Right {
        TestJson.JTuple19((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19))
      }
    }
    "Tuple20 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 20).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple20Codec) shouldBe Right {
        TestJson.JTuple20((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20))
      }
    }
    "Tuple21 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 21).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple21Codec) shouldBe Right {
        TestJson.JTuple21((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21))
      }
    }
    "Tuple22 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 22).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(JTuple22Codec) shouldBe Right {
        TestJson.JTuple22((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
      }
    }

    "Tuple2 deserialize" in {
      s"""{"_1":1,"_2":2}""".as[(Int, Int)] shouldBe Right {
        (1, 2)
      }
    }
    "Tuple3 deserialize" in {
      s"""{${
        (1 to 3).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int)] shouldBe Right {
        (1, 2, 3)
      }
    }
    "Tuple4 deserialize" in {
      s"""{${
        (1 to 4).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4)
      }
    }
    "Tuple5 deserialize" in {
      s"""{${
        (1 to 5).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5)
      }
    }
    "Tuple6 deserialize" in {
      s"""{${
        (1 to 6).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6)
      }
    }
    "Tuple7 deserialize" in {
      s"""{${
        (1 to 7).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7)
      }
    }
    "Tuple8 deserialize" in {
      s"""{${
        (1 to 8).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8)
      }
    }
    "Tuple9 deserialize" in {
      s"""{${
        (1 to 9).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9)
      }
    }
    "Tuple10 deserialize" in {
      s"""{${
        (1 to 10).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
      }
    }
    "Tuple11 deserialize" in {
      s"""{${
        (1 to 11).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
      }
    }
    "Tuple12 deserialize" in {
      s"""{${
        (1 to 12).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
      }
    }
    "Tuple13 deserialize" in {
      s"""{${
        (1 to 13).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
      }
    }
    "Tuple14 deserialize" in {
      s"""{${
        (1 to 14).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
      }
    }
    "Tuple15 deserialize" in {
      s"""{${
        (1 to 15).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
      }
    }
    "Tuple16 deserialize" in {
      s"""{${
        (1 to 16).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)
      }
    }
    "Tuple17 deserialize" in {
      s"""{${
        (1 to 17).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
      }
    }
    "Tuple18 deserialize" in {
      s"""{${
        (1 to 18).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)
      }
    }
    "Tuple19 deserialize" in {
      s"""{${
        (1 to 19).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
      }
    }
    "Tuple20 deserialize" in {
      s"""{${
        (1 to 20).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
      }
    }
    "Tuple21 deserialize" in {
      s"""{${
        (1 to 21).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21)
      }
    }
    "Tuple22 deserialize" in {
      s"""{${
        (1 to 22).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}""".as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)] shouldBe Right {
        (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
      }
    }
  }

  "json serialize" should {
    "Tuple2 wrapper serialize" in {
      TestJson.JTuple2((1, 2)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 2).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple3 wrapper serialize" in {
      TestJson.JTuple3((1, 2, 3)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 3).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple4 wrapper serialize" in {
      TestJson.JTuple4((1, 2, 3, 4)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 4).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple5 wrapper serialize" in {
      TestJson.JTuple5((1, 2, 3, 4, 5)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 5).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple6 wrapper serialize" in {
      TestJson.JTuple6((1, 2, 3, 4, 5, 6)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 6).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple7 wrapper serialize" in {
      TestJson.JTuple7((1, 2, 3, 4, 5, 6, 7)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 7).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple8 wrapper serialize" in {
      TestJson.JTuple8((1, 2, 3, 4, 5, 6, 7, 8)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 8).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple9 wrapper serialize" in {
      TestJson.JTuple9((1, 2, 3, 4, 5, 6, 7, 8, 9)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 9).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple10 wrapper serialize" in {
      TestJson.JTuple10((1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 10).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple11 wrapper serialize" in {
      TestJson.JTuple11((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 11).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple12 wrapper serialize" in {
      TestJson.JTuple12((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 12).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple13 wrapper serialize" in {
      TestJson.JTuple13((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 13).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple14 wrapper serialize" in {
      TestJson.JTuple14((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 14).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple15 wrapper serialize" in {
      TestJson.JTuple15((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 15).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple16 wrapper serialize" in {
      TestJson.JTuple16((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 16).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple17 wrapper serialize" in {
      TestJson.JTuple17((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 17).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple18 wrapper serialize" in {
      TestJson.JTuple18((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 18).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple19 wrapper serialize" in {
      TestJson.JTuple19((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 19).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple20 wrapper serialize" in {
      TestJson.JTuple20((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 20).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple21 wrapper serialize" in {
      TestJson.JTuple21((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 21).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }
    "Tuple22 wrapper serialize" in {
      TestJson.JTuple22((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)).toJson.toString shouldBe {
        s"""{value -> {${
          (1 to 22).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}}"""
      }
    }

    "Tuple2 serialize" in {
      (1, 2).toJson.toString shouldBe {
        s"""{${
          (1 to 2).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple3 serialize" in {
      (1, 2, 3).toJson.toString shouldBe {
        s"""{${
          (1 to 3).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple4 serialize" in {
      (1, 2, 3, 4).toJson.toString shouldBe {
        s"""{${
          (1 to 4).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple5 serialize" in {
      (1, 2, 3, 4, 5).toJson.toString shouldBe {
        s"""{${
          (1 to 5).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple6 serialize" in {
      (1, 2, 3, 4, 5, 6).toJson.toString shouldBe {
        s"""{${
          (1 to 6).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple7 serialize" in {
      (1, 2, 3, 4, 5, 6, 7).toJson.toString shouldBe {
        s"""{${
          (1 to 7).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple8 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8).toJson.toString shouldBe {
        s"""{${
          (1 to 8).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple9 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9).toJson.toString shouldBe {
        s"""{${
          (1 to 9).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple10 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toJson.toString shouldBe {
        s"""{${
          (1 to 10).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple11 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).toJson.toString shouldBe {
        s"""{${
          (1 to 11).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple12 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).toJson.toString shouldBe {
        s"""{${
          (1 to 12).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple13 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13).toJson.toString shouldBe {
        s"""{${
          (1 to 13).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple14 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14).toJson.toString shouldBe {
        s"""{${
          (1 to 14).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple15 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).toJson.toString shouldBe {
        s"""{${
          (1 to 15).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple16 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16).toJson.toString shouldBe {
        s"""{${
          (1 to 16).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple17 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17).toJson.toString shouldBe {
        s"""{${
          (1 to 17).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple18 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).toJson.toString shouldBe {
        s"""{${
          (1 to 18).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple19 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19).toJson.toString shouldBe {
        s"""{${
          (1 to 19).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple20 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20).toJson.toString shouldBe {
        s"""{${
          (1 to 20).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple21 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21).toJson.toString shouldBe {
        s"""{${
          (1 to 21).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
    "Tuple22 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22).toJson.toString shouldBe {
        s"""{${
          (1 to 22).map { x =>
            s"""_$x -> $x"""
          }.mkString(", ")
        }}"""
      }
    }
  }
}
