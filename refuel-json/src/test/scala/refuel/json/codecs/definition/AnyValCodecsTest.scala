package refuel.json.codecs.definition

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{CodecDef, JsonTransform}
import refuel.json.model.TestJson._

class AnyValCodecsTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {
  "json deserialize" should {

    "Int deserialize" in {
      s"""{"value":3}""".as[JInt](CaseClassCodec.from[JInt]) shouldBe Right {
        JInt(3)
      }
    }
    "Int deserialize minus" in {
      s"""{"value":-3}""".as[JInt](CaseClassCodec.from[JInt]) shouldBe Right {
        JInt(-3)
      }
    }

    "Long deserialize" in {
      s"""{"value":9223372036854775807}""".as[JLong](CaseClassCodec.from[JLong]) shouldBe Right {
        JLong(Long.MaxValue)
      }
    }

    "String deserialize" in {
      s"""{"value":"body"}""".as[JString](CaseClassCodec.from[JString]) shouldBe Right {
        JString("body")
      }
    }
    "String deserialize ^ escaped 1" in {
      s"""{"value":"bo\\"d'y"}""".as[JString](CaseClassCodec.from[JString]) shouldBe Right {
        JString("bo\\\"d'y")
      }
    }
    "String deserialize ^ escaped 2" in {
      s"""{"value":"bo\\\\\\"d'y"}""".as[JString](CaseClassCodec.from[JString]) shouldBe Right {
        JString("bo\\\\\\\"d'y")
      }
    }

    "Float deserialize" in {
      s"""{"value":0.123}""".as[JFloat](CaseClassCodec.from[JFloat]) shouldBe Right {
        JFloat(0.123f)
      }
    }
    "Float deserialize ^ minus" in {
      s"""{"value":-0.123}""".as[JFloat](CaseClassCodec.from[JFloat]) shouldBe Right {
        JFloat(-0.123f)
      }
    }

    "Double deserialize" in {
      s"""{"value":0.123}""".as[JDouble](CaseClassCodec.from[JDouble]) shouldBe Right {
        JDouble(0.123d)
      }
    }
    "Double deserialize ^ minus" in {
      s"""{"value":-0.123}""".as[JDouble](CaseClassCodec.from[JDouble]) shouldBe Right {
        JDouble(-0.123d)
      }
    }

    "Boolean deserialize true" in {
      s"""{"value":true}""".as[JBoolean](CaseClassCodec.from[JBoolean]) shouldBe Right {
        JBoolean(true)
      }
    }
    "Boolean deserialize false" in {
      s"""{"value":false}""".as[JBoolean](CaseClassCodec.from[JBoolean]) shouldBe Right {
        JBoolean(false)
      }
    }
  }

  "json serialize" should {
    "Int serialize" in {
      val str: JInt = JInt(3)
      str.toJString(CaseClassCodec.from[JInt]) shouldBe s"""{"value":3}"""
    }
    "Int serialize minus" in {
      val str: JInt = JInt(-3)
      str.toJString(CaseClassCodec.from[JInt]) shouldBe s"""{"value":-3}"""
    }

    "Long serialize" in {
      val str: JLong = JLong(Long.MaxValue)
      str.toJString(CaseClassCodec.from[JLong]).toString shouldBe s"""{"value":9223372036854775807}"""
    }

    "String serialize" in {
      val str: JString = JString("body")
      str.toJString(CaseClassCodec.from[JString]).toString shouldBe s"""{"value":"body"}"""
    }
    "String serialize ^ escaped 1" in {
      val str: JString = JString("bo\\\"d'y")
      str.toJString(CaseClassCodec.from[JString]).toString shouldBe s"""{"value":"bo\\\\\\"d'y"}"""
    }
    "String serialize ^ escaped 2" in {
      val str: JString = JString("bo\\\"d'y")
      str.toJString(CaseClassCodec.from[JString]).toString shouldBe s"""{"value":"bo\\\\\\"d'y"}"""
    }

    "Float serialize" in {
      val str: JFloat = JFloat(0.123f)
      str.toJString(CaseClassCodec.from[JFloat]).toString shouldBe s"""{"value":0.123}"""
    }
    "Float serialize ^ minus" in {
      val str: JFloat = JFloat(-0.123f)
      str.toJString(CaseClassCodec.from[JFloat]).toString shouldBe s"""{"value":-0.123}"""
    }

    "Double serialize" in {
      val str: JDouble = JDouble(0.123d)
      str.toJString(CaseClassCodec.from[JDouble]).toString shouldBe s"""{"value":0.123}"""
    }
    "Double serialize ^ minus" in {
      val str: JDouble = JDouble(-0.123d)
      str.toJString(CaseClassCodec.from[JDouble]).toString shouldBe s"""{"value":-0.123}"""
    }

    "Boolean serialize true" in {
      val str: JBoolean = JBoolean(true)
      str.toJString(CaseClassCodec.from[JBoolean]).toString shouldBe s"""{"value":true}"""
    }
    "Boolean serialize false" in {
      val str: JBoolean = JBoolean(false)
      str.toJString(CaseClassCodec.from[JBoolean]).toString shouldBe s"""{"value":false}"""
    }
  }
}
