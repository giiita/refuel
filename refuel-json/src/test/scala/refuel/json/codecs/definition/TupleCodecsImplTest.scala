package refuel.json.codecs.definition

import refuel.json.JsParser
import refuel.json.codecs.factory.CaseClassCodec
import refuel.json.model.TestJson
import refuel.json.model.TestJson.{JTuple2, JTuple22}
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

class TupleCodecsImplTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JsParser {
  "json deserialize" should {
    "Tuple2 wrapper deserialize" in {
      s"""{"value":{"_1":1,"_2":2}}""".as(CaseClassCodec.from[JTuple2]) shouldBe Right {
        TestJson.JTuple2((1, 2))
      }
    }
    "Tuple22 wrapper deserialize" in {
      s"""{"value":{${
        (1 to 22).map { x =>
          s""""_$x":$x"""
        }.mkString(",")
      }}}""".as(CaseClassCodec.from[JTuple22]) shouldBe Right {
        TestJson.JTuple22((1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22))
      }
    }

    "Tuple2 deserialize" in {
      s"""{"_1":1,"_2":2}""".as[(Int, Int)] shouldBe Right {
        (1, 2)
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
}
