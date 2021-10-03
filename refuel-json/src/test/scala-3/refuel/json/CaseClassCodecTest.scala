package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.CaseClassCodecTest.{Id}
import refuel.json.JsonVal.{JsAny, JsObject, JsString}
import refuel.json.codecs.Codec
import refuel.json.{JsonTransform, JsonVal}

import scala.compiletime.erasedValue
import scala.compiletime.package$package.{constValue, erasedValue}
import scala.util.{Failure, Success, Try}
import JsonTransform.{*, given}

object CaseClassCodecTest {

  opaque type IdD = Long
  opaque type Name = String

  val codec: Codec[Seq[Id]] = Derive[Seq[Id]]

  case class BBBB(id: Long, value: String)

  case class CCCC(value: Map[String, BBBB])

  case class DDDD(id: Long, value: CCCC)

  case class A(value: String)

  case class B(a: A)

  case class C(b: B)

  case class D(c: C)

  case class AAA(aaaId: Long, int: Int)

  class BBB(val bbbId: Long, val aaa: Option[AAA]) {
    override def hashCode(): Int = 31 * 1

    override def equals(obj: Any): Boolean = obj match {
      case x: BBB => bbbId == x.bbbId && aaa == x.aaa
      case _      => false
    }
  }

  given Codec[BBB] = Construct[BBB](
    { bf =>
      for {
        bbbId <- bf.named("bbbId").readAs[Long]
        aaa <- bf.named("aaa").readAs(using ReadOf.option(using CaseClass[AAA]))
      } yield new BBB(bbbId, aaa)
    },
    { t =>
      JsObject()
        .++(JsString("bbbId"))
        .flatMap(_ ++ JsAny(t.bbbId))
        .flatMap(_ ++ JsString("aaa"))
        .flatMap(_ ++ t.aaa.writeAs(using CaseClass[AAA].cmap(WriteOf.option(using _))))
    }
  )

  case class CCC(cccId: Long, bbbs: Option[Seq[BBB]] = None)

  case class DDD(dddId: Long, ccc: CCC)


  case class EEE(eeeId: Long, ddd: DDD)

  case class Id(v: String, s: String)

  case class Foo(a: String, b: String)

  object IdD {
    def apply(v: Long): IdD = v
  }

  object Name {
    def apply(v: String): Name = v
    val codec: Codec[Name] = Derive[String]
  }
}

class CaseClassCodecTest extends AsyncWordSpec with Matchers with Diagrams {
  import refuel.json.CaseClassCodecTest.*

  "Load implicit codec" should {
    "anyval Long codec" in {
      Name("foo").writeAsString(using Name.codec).flatMap(_.readAs(using Name.codec)) shouldBe Success(Name("foo"))
    }
    "single member codec type" in {
      s"""{"c": {"b": {"a": {"value": "hoge"}}}}""".readAs(using CaseClass[D]) match {
        case Failure(e)  => fail("Expect failed", e)
        case Success(x) => x shouldBe D(C(B(A("hoge"))))
      }
    }
    "custom implicit codec type" in {
      val r =
        s"""{"eeeId":1,"ddd":{"dddId":2,"ccc":{"cccId":3,"bbbs":[{"bbbId":4},{"bbbId":5,"aaa":{"aaaId":7,"int":8}},{"bbbId":6}]}}}"""
          .readAs(using Derive[EEE]).get

      r.eeeId shouldBe 1
      r.ddd.dddId shouldBe 2
      r.ddd.ccc.cccId shouldBe 3
      r.ddd.ccc.bbbs.get.map(_.bbbId) shouldBe Seq(4, 5, 6)
      r.ddd.ccc.bbbs.get.map(_.aaa) shouldBe Seq(None, Some(AAA(7, 8)), None)
    }

    "implicit override codec" in {
      val bbbs =
        Seq(new BBB(4, None), new BBB(5, Some(AAA(7, 8))), new BBB(6, None))

      given Codec[DDD] = Construct(
        { json =>
          Success(DDD(2, CCC(3, Some(bbbs))))
        },
        { e =>
          ???
        }
      )

      s"""{"eeeId":1,"ddd":{"overwrite":"insertion value"}}""".readAs(
        using Derive[EEE]
      ) shouldBe Success {
        EEE(1, DDD(2, CCC(3, Some(bbbs))))
      }
    }

    "Used implicitly codec of refuel" in {
      s"""{"id": 0, "value": {"value": {"hoge": {"id": 1, "value": "AAA"}, "huga": {"id": 2, "value": "BBB"}}}}"""
        .readAs(using Derive[DDDD]) shouldBe Success {
        DDDD(
          0,
          CCCC(Map("hoge" -> BBBB(1, "AAA"), "huga" -> BBBB(2, "BBB")))
        )
      }
    }
  }
}
