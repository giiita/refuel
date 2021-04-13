package refuel.json.codecs.definition

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{Codec, CodecDef, JsonTransform}
import refuel.json.model.TestJson
import refuel.json.model.TestJson._

class TupleCodecsImplTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {

  import scala.language.implicitConversions

  implicit lazy val JTuple2Codec: Codec[JTuple2]   = CaseClassCodec.from[JTuple2]
  implicit lazy val JTuple3Codec: Codec[JTuple3]   = CaseClassCodec.from[JTuple3]
  implicit lazy val JTuple4Codec: Codec[JTuple4]   = CaseClassCodec.from[JTuple4]
  implicit lazy val JTuple5Codec: Codec[JTuple5]   = CaseClassCodec.from[JTuple5]
  implicit lazy val JTuple6Codec: Codec[JTuple6]   = CaseClassCodec.from[JTuple6]
  implicit lazy val JTuple7Codec: Codec[JTuple7]   = CaseClassCodec.from[JTuple7]
  implicit lazy val JTuple8Codec: Codec[JTuple8]   = CaseClassCodec.from[JTuple8]
  implicit lazy val JTuple9Codec: Codec[JTuple9]   = CaseClassCodec.from[JTuple9]
  implicit lazy val JTuple10Codec: Codec[JTuple10] = CaseClassCodec.from[JTuple10]
  implicit lazy val JTuple11Codec: Codec[JTuple11] = CaseClassCodec.from[JTuple11]
  implicit lazy val JTuple12Codec: Codec[JTuple12] = CaseClassCodec.from[JTuple12]
  implicit lazy val JTuple13Codec: Codec[JTuple13] = CaseClassCodec.from[JTuple13]
  implicit lazy val JTuple14Codec: Codec[JTuple14] = CaseClassCodec.from[JTuple14]
  implicit lazy val JTuple15Codec: Codec[JTuple15] = CaseClassCodec.from[JTuple15]
  implicit lazy val JTuple16Codec: Codec[JTuple16] = CaseClassCodec.from[JTuple16]
  implicit lazy val JTuple17Codec: Codec[JTuple17] = CaseClassCodec.from[JTuple17]
  implicit lazy val JTuple18Codec: Codec[JTuple18] = CaseClassCodec.from[JTuple18]
  implicit lazy val JTuple19Codec: Codec[JTuple19] = CaseClassCodec.from[JTuple19]
  implicit lazy val JTuple20Codec: Codec[JTuple20] = CaseClassCodec.from[JTuple20]
  implicit lazy val JTuple21Codec: Codec[JTuple21] = CaseClassCodec.from[JTuple21]
  implicit lazy val JTuple22Codec: Codec[JTuple22] = CaseClassCodec.from[JTuple22]

  "json deserialize" should {
    "Tuple2 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 2).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple2Codec).isRight)
    }
    "Tuple3 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 3).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple3Codec).isRight)
    }
    "Tuple4 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 4).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple4Codec).isRight)
    }
    "Tuple5 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 5).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple5Codec).isRight)
    }
    "Tuple6 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 6).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple6Codec).isRight)
    }
    "Tuple7 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 7).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple7Codec).isRight)
    }
    "Tuple8 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 8).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple8Codec).isRight)
    }
    "Tuple9 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 9).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple9Codec).isRight)
    }
    "Tuple10 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 10).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple10Codec).isRight)
    }
    "Tuple11 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 11).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple11Codec).isRight)
    }
    "Tuple12 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 12).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple12Codec).isRight)
    }
    "Tuple13 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 13).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple13Codec).isRight)
    }
    "Tuple14 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 14).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple14Codec).isRight)
    }
    "Tuple15 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 15).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple15Codec).isRight)
    }
    "Tuple16 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 16).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple16Codec).isRight)
    }
    "Tuple17 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 17).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple17Codec).isRight)
    }
    "Tuple18 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 18).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple18Codec).isRight)
    }
    "Tuple19 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 19).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple19Codec).isRight)
    }
    "Tuple20 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 20).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple20Codec).isRight)
    }
    "Tuple21 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 21).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple21Codec).isRight)
    }
    "Tuple22 wrapper deserialize" in {
      assert(s"""{"value":{${(1 to 22).map { x => s""""_$x":$x""" }.mkString(",")}}}""".as(JTuple22Codec).isRight)
    }

    "Tuple2 deserialize" in {
      assert(s"""{"_1":1,"_2":2}""".as[(Int, Int)].isRight)
    }
    "Tuple3 deserialize" in {
      assert(s"""{${(1 to 3).map { x => s""""_$x":$x""" }.mkString(",")}}""".as[(Int, Int, Int)].isRight)
    }
    "Tuple4 deserialize" in {
      assert(s"""{${(1 to 4).map { x => s""""_$x":$x""" }.mkString(",")}}""".as[(Int, Int, Int, Int)].isRight)
    }
    "Tuple5 deserialize" in {
      assert(s"""{${(1 to 5).map { x => s""""_$x":$x""" }.mkString(",")}}""".as[(Int, Int, Int, Int, Int)].isRight)
    }
    "Tuple6 deserialize" in {
      assert(
        s"""{${(1 to 6).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple7 deserialize" in {
      assert(
        s"""{${(1 to 7).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple8 deserialize" in {
      assert(
        s"""{${(1 to 8).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple9 deserialize" in {
      assert(
        s"""{${(1 to 9).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple10 deserialize" in {
      assert(
        s"""{${(1 to 10).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple11 deserialize" in {
      assert(
        s"""{${(1 to 11).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple12 deserialize" in {
      assert(
        s"""{${(1 to 12).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple13 deserialize" in {
      assert(
        s"""{${(1 to 13).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple14 deserialize" in {
      assert(
        s"""{${(1 to 14).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple15 deserialize" in {
      assert(
        s"""{${(1 to 15).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple16 deserialize" in {
      assert(
        s"""{${(1 to 16).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple17 deserialize" in {
      assert(
        s"""{${(1 to 17).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple18 deserialize" in {
      assert(
        s"""{${(1 to 18).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[(Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)]
          .isRight
      )
    }
    "Tuple19 deserialize" in {
      assert(
        s"""{${(1 to 19).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[
            (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)
          ]
          .isRight
      )
    }
    "Tuple20 deserialize" in {
      assert(
        s"""{${(1 to 20).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[
            (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)
          ]
          .isRight
      )
    }
    "Tuple21 deserialize" in {
      assert(
        s"""{${(1 to 21).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[
            (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int)
          ]
          .isRight
      )
    }
    "Tuple22 deserialize" in {
      assert(
        s"""{${(1 to 22).map { x => s""""_$x":$x""" }.mkString(",")}}"""
          .as[
            (
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int,
                Int
            )
          ]
          .isRight
      )
    }
  }

  "json serialize" should {
    "Tuple2 wrapper serialize" in {
      TestJson.JTuple2((1, 2)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 2).mkString(",")}]}"""
      }
    }
    "Tuple3 wrapper serialize" in {
      TestJson.JTuple3((1, 2, 3)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 3).mkString(",")}]}"""
      }
    }
    "Tuple4 wrapper serialize" in {
      TestJson.JTuple4((1, 2, 3, 4)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 4).mkString(",")}]}"""
      }
    }
    "Tuple5 wrapper serialize" in {
      TestJson.JTuple5((1, 2, 3, 4, 5)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 5).mkString(",")}]}"""
      }
    }
    "Tuple6 wrapper serialize" in {
      TestJson.JTuple6((1, 2, 3, 4, 5, 6)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 6).mkString(",")}]}"""
      }
    }
    "Tuple7 wrapper serialize" in {
      TestJson.JTuple7((1, 2, 3, 4, 5, 6, 7)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 7).mkString(",")}]}"""
      }
    }
    "Tuple8 wrapper serialize" in {
      TestJson.JTuple8((1, 2, 3, 4, 5, 6, 7, 8)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 8).mkString(",")}]}"""
      }
    }
    "Tuple9 wrapper serialize" in {
      TestJson.JTuple9((1, 2, 3, 4, 5, 6, 7, 8, 9)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 9).mkString(",")}]}"""
      }
    }
    "Tuple10 wrapper serialize" in {
      TestJson.JTuple10((1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 10).mkString(",")}]}"""
      }
    }
    "Tuple11 wrapper serialize" in {
      TestJson.JTuple11((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 11).mkString(",")}]}"""
      }
    }
    "Tuple12 wrapper serialize" in {
      TestJson.JTuple12((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 12).mkString(",")}]}"""
      }
    }
    "Tuple13 wrapper serialize" in {
      TestJson.JTuple13((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 13).mkString(",")}]}"""
      }
    }
    "Tuple14 wrapper serialize" in {
      TestJson.JTuple14((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 14).mkString(",")}]}"""
      }
    }
    "Tuple15 wrapper serialize" in {
      TestJson.JTuple15((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 15).mkString(",")}]}"""
      }
    }
    "Tuple16 wrapper serialize" in {
      TestJson.JTuple16((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 16).mkString(",")}]}"""
      }
    }
    "Tuple17 wrapper serialize" in {
      TestJson.JTuple17((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 17).mkString(",")}]}"""
      }
    }
    "Tuple18 wrapper serialize" in {
      TestJson.JTuple18((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 18).mkString(",")}]}"""
      }
    }
    "Tuple19 wrapper serialize" in {
      TestJson.JTuple19((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)).toJson.toString shouldBe {
        s"""{"value":[${(1 to 19).mkString(",")}]}"""
      }
    }
    "Tuple20 wrapper serialize" in {
      TestJson
        .JTuple20((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20))
        .toJson
        .toString shouldBe {
        s"""{"value":[${(1 to 20).mkString(",")}]}"""
      }
    }
    "Tuple21 wrapper serialize" in {
      TestJson
        .JTuple21((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21))
        .toJson
        .toString shouldBe {
        s"""{"value":[${(1 to 21).mkString(",")}]}"""
      }
    }
    "Tuple22 wrapper serialize" in {
      TestJson
        .JTuple22((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
        .toJson
        .toString shouldBe {
        s"""{"value":[${(1 to 22).mkString(",")}]}"""
      }
    }

    "Tuple2 serialize" in {
      (1, 2).toJson.toString shouldBe {
        s"""[${(1 to 2).mkString(",")}]"""
      }
    }
    "Tuple3 serialize" in {
      (1, 2, 3).toJson.toString shouldBe {
        s"""[${(1 to 3).mkString(",")}]"""
      }
    }
    "Tuple4 serialize" in {
      (1, 2, 3, 4).toJson.toString shouldBe {
        s"""[${(1 to 4).mkString(",")}]"""
      }
    }
    "Tuple5 serialize" in {
      (1, 2, 3, 4, 5).toJson.toString shouldBe {
        s"""[${(1 to 5).mkString(",")}]"""
      }
    }
    "Tuple6 serialize" in {
      (1, 2, 3, 4, 5, 6).toJson.toString shouldBe {
        s"""[${(1 to 6).mkString(",")}]"""
      }
    }
    "Tuple7 serialize" in {
      (1, 2, 3, 4, 5, 6, 7).toJson.toString shouldBe {
        s"""[${(1 to 7).mkString(",")}]"""
      }
    }
    "Tuple8 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8).toJson.toString shouldBe {
        s"""[${(1 to 8).mkString(",")}]"""
      }
    }
    "Tuple9 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9).toJson.toString shouldBe {
        s"""[${(1 to 9).mkString(",")}]"""
      }
    }
    "Tuple10 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toJson.toString shouldBe {
        s"""[${(1 to 10).mkString(",")}]"""
      }
    }
    "Tuple11 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).toJson.toString shouldBe {
        s"""[${(1 to 11).mkString(",")}]"""
      }
    }
    "Tuple12 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).toJson.toString shouldBe {
        s"""[${(1 to 12).mkString(",")}]"""
      }
    }
    "Tuple13 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13).toJson.toString shouldBe {
        s"""[${(1 to 13).mkString(",")}]"""
      }
    }
    "Tuple14 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14).toJson.toString shouldBe {
        s"""[${(1 to 14).mkString(",")}]"""
      }
    }
    "Tuple15 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).toJson.toString shouldBe {
        s"""[${(1 to 15).mkString(",")}]"""
      }
    }
    "Tuple16 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16).toJson.toString shouldBe {
        s"""[${(1 to 16).mkString(",")}]"""
      }
    }
    "Tuple17 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17).toJson.toString shouldBe {
        s"""[${(1 to 17).mkString(",")}]"""
      }
    }
    "Tuple18 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).toJson.toString shouldBe {
        s"""[${(1 to 18).mkString(",")}]"""
      }
    }
    "Tuple19 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19).toJson.toString shouldBe {
        s"""[${(1 to 19).mkString(",")}]"""
      }
    }
    "Tuple20 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20).toJson.toString shouldBe {
        s"""[${(1 to 20).mkString(",")}]"""
      }
    }
    "Tuple21 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21).toJson.toString shouldBe {
        s"""[${(1 to 21).mkString(",")}]"""
      }
    }
    "Tuple22 serialize" in {
      (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22).toJson.toString shouldBe {
        s"""[${(1 to 22).mkString(",")}]"""
      }
    }
  }
}
