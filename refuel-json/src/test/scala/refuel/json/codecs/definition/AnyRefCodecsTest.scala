package refuel.json.codecs.definition

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{CodecDef, JsonTransform}
import refuel.json.model.TestJson._

class AnyRefCodecsTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {

  "json deserialize" should {
    "Option Int deserialize ^ Some" in {
      val x = s"""{"value":3}""".jsonTree
      s"""{"value":3}""".as[JOptInt](CaseClassCodec.from[JOptInt]) shouldBe Right {
        JOptInt(Some(3))
      }
    }
    "Option Int deserialize ^ None" in {
      s"""{}""".as[JOptInt](CaseClassCodec.from[JOptInt]) shouldBe Right {
        JOptInt(None)
      }
    }


    "Option Long deserialize ^ Some" in {
      s"""{"value":3}""".as[JOptLong](CaseClassCodec.from[JOptLong]) shouldBe Right {
        JOptLong(Some(3L))
      }
    }
    "Option Long deserialize ^ None" in {
      s"""{}""".as[JOptLong](CaseClassCodec.from[JOptLong]) shouldBe Right {
        JOptLong(None)
      }
    }


    "Option float deserialize ^ Some" in {
      s"""{"value":0.123}""".as[JOptFloat](CaseClassCodec.from[JOptFloat]) shouldBe Right {
        JOptFloat(Some(0.123F))
      }
    }
    "Option float deserialize ^ Some minus" in {
      s"""{"value":-0.123}""".as[JOptFloat](CaseClassCodec.from[JOptFloat]) shouldBe Right {
        JOptFloat(Some(-0.123F))
      }
    }
    "Option float deserialize ^ None" in {
      s"""{}""".as[JOptFloat](CaseClassCodec.from[JOptFloat]) shouldBe Right {
        JOptFloat(None)
      }
    }


    "Option double deserialize ^ Some" in {
      s"""{"value":0.123}""".as[JOptDouble](CaseClassCodec.from[JOptDouble]) shouldBe Right {
        JOptDouble(Some(0.123D))
      }
    }
    "Option double deserialize ^ Some minus" in {
      s"""{"value":-0.123}""".as[JOptDouble](CaseClassCodec.from[JOptDouble]) shouldBe Right {
        JOptDouble(Some(-0.123D))
      }
    }
    "Option double deserialize ^ None" in {
      s"""{}""".as[JOptDouble](CaseClassCodec.from[JOptDouble]) shouldBe Right {
        JOptDouble(None)
      }
    }


    "Option String deserialize" in {
      s"""{"value":"body"}""".as[JOptString](CaseClassCodec.from[JOptString]) shouldBe Right {
        JOptString(Some("body"))
      }
    }
    "Option String deserialize None" in {
      s"""{}""".as(CaseClassCodec.from[JOptString]) shouldBe Right {
        JOptString(None)
      }
    }
    "Option String deserialize ^ escaped 1" in {
      s"""{"value":"bo\\\\\\"d'y"}""".as(CaseClassCodec.from[JOptString]) shouldBe Right {
        JOptString(Some("bo\\\\\\\"d'y"))
      }
    }


    "Option Boolean deserialize true" in {
      s"""{"value":true}""".as(CaseClassCodec.from[JOptBoolean]) shouldBe Right {
        JOptBoolean(Some(true))
      }
    }
    "Option Boolean deserialize None" in {
      s"""{}""".as(CaseClassCodec.from[JOptBoolean]) shouldBe Right {
        JOptBoolean(None)
      }
    }
  }

  "json serialize" should {
    "Option Int serialize ^ Some" in {
      val str: JOptInt = JOptInt(Some(3))
      str.toJString(CaseClassCodec.from[JOptInt]).toString shouldBe s"""{"value":3}"""
    }
    "Option Int serialize ^ None" in {
      val str: JOptInt = JOptInt(None)
      str.toJString(CaseClassCodec.from[JOptInt]).toString shouldBe s"""{}"""
    }


    "Option Long serialize ^ Some" in {
      val str: JOptLong = JOptLong(Some(3L))
      str.toJString(CaseClassCodec.from[JOptLong]).toString shouldBe s"""{"value":3}"""
    }
    "Option Long serialize ^ None" in {
      val str: JOptLong = JOptLong(None)
      str.toJString(CaseClassCodec.from[JOptLong]).toString shouldBe s"""{}"""
    }


    "Option float serialize ^ Some" in {
      val str: JOptFloat = JOptFloat(Some(0.123F))
      str.toJString(CaseClassCodec.from[JOptFloat]).toString shouldBe s"""{"value":0.123}"""
    }
    "Option float serialize ^ Some minus" in {
      val str: JOptFloat = JOptFloat(Some(-0.123F))
      str.toJString(CaseClassCodec.from[JOptFloat]).toString shouldBe s"""{"value":-0.123}"""
    }
    "Option float serialize ^ None" in {
      val str: JOptFloat = JOptFloat(None)
      str.toJString(CaseClassCodec.from[JOptFloat]).toString shouldBe s"""{}"""
    }


    "Option double serialize ^ Some" in {
      val str: JOptDouble = JOptDouble(Some(0.123D))
      str.toJString(CaseClassCodec.from[JOptDouble]).toString shouldBe s"""{"value":0.123}"""
    }
    "Option double serialize ^ Some minus" in {
      val str: JOptDouble = JOptDouble(Some(-0.123D))
      str.toJString(CaseClassCodec.from[JOptDouble]).toString shouldBe s"""{"value":-0.123}"""
    }
    "Option double serialize ^ None" in {
      val str: JOptDouble = JOptDouble(None)
      str.toJString(CaseClassCodec.from[JOptDouble]).toString shouldBe s"""{}"""
    }


    "Option String serialize" in {
      val str: JOptString = JOptString(Some("body"))
      str.toJString(CaseClassCodec.from[JOptString]).toString shouldBe s"""{"value":"body"}"""
    }
    "Option String serialize None" in {
      val str: JOptString = JOptString(None)
      str.toJString(CaseClassCodec.from[JOptString]).toString shouldBe s"""{}"""
    }
    "Option String serialize ^ escaped 1" in {
      val str: JOptString = JOptString(Some("bo\\\"d'y"))
      str.toJString(CaseClassCodec.from[JOptString]) shouldBe "{\"value\":\"bo\\\\\\\"d'y\"}"
    }


    "Option Boolean serialize true" in {
      val str: JOptBoolean = JOptBoolean(Some(true))
      str.toJString(CaseClassCodec.from[JOptBoolean]).toString shouldBe s"""{"value":true}"""
    }
    "Option Boolean serialize None" in {
      val str: JOptBoolean = JOptBoolean(None)
      str.toJString(CaseClassCodec.from[JOptBoolean]).toString shouldBe s"""{}"""
    }
  }
}
