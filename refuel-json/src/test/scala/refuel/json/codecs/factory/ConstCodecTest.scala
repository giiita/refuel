package refuel.json.codecs.factory

import refuel.json.JsParser
import refuel.json.codecs.factory.ConstCodecTest._
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

class ConstCodecTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JsParser {
  "Construct based codec inspection" should {
    "tuple 1 construnct codec" in {
      s"""{"name": "xxxxx"}""".as(ConstCodec.from("name")(Const1.apply)(Const1.unapply)) match {
        case Left(_)  => fail()
        case Right(r) => r.value shouldBe "xxxxx"
      }
    }
    "Wrapped type codec" in {
      s"""{"hoge":{"value":"xxx"}}""".as(ConstCodec.from("hoge")(WrappedType.apply)(WrappedType.unapply)) match {
        case Left(_)  => fail()
        case Right(r) => r shouldBe WrappedType(InnerType("xxx"))
      }
    }
    "tuple 2 construnct codec" in {
      s"""{"bambi": 11, "bombi": "xxx"}""".as(ConstCodec.from("bambi", "bombi")(Const2.apply)(Const2.unapply)) match {
        case Left(_)  => fail()
        case Right(r) => r shouldBe Const2(11, "xxx")
      }
    }
    "tuple 22 construnct codec" in {
      s"""{"aimpl": 1, "bimpl": 2, "cimpl": 3, "dimpl": 4, "eimpl": 5, "fimpl": 6, "gimpl": 7, "himpl": 8, "iimpl": 9, "jimpl": 10, "kimpl": 11, "limpl": 12, "mimpl": 13, "nimpl": 14, "oimpl": 15, "pimpl": 16, "qimpl": 17, "rimpl": 18, "simpl": 19, "timpl": 20, "uimpl": 21, "vimpl": 22}"""
        .as(ConstCodec.from("aimpl", "bimpl", "cimpl", "dimpl", "eimpl", "fimpl", "gimpl", "himpl", "iimpl", "jimpl", "kimpl", "limpl", "mimpl", "nimpl", "oimpl", "pimpl", "qimpl", "rimpl", "simpl", "timpl", "uimpl", "vimpl")(Const22.apply)(Const22.unapply)) match {
        case Left(_)  => fail()
        case Right(r) => r shouldBe Const22(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
      }
    }
  }


  "Customized case" should {
    "sss" in {
      s"""{"hoge": {"min": 0, "max": 1}}""".as(
        ConstCodec.from[From, To]("hoge"){
          case From(Some(a), Some(b)) => To(a, b)
        }{
          case To(a, b) => Some(From(Some(a), Some(b)))
        }
      ) shouldBe Right(To(0, 1))
    }
    "to" in {
      To(0, 1).toJson(
        ConstCodec.from[From, To]("hoge"){
          case From(Some(a), Some(b)) => To(a, b)
        }{
          case To(a, b) => Some(From(Some(a), Some(b)))
        }
      ).toString shouldBe s"""{"hoge":{"min":0,"max":1}}"""
    }
  }

}

object ConstCodecTest {

  case class To(min: Int, max: Int)

  case class From(min: Option[Int], max: Option[Int])

  case class WrappedType(wrap: InnerType)

  case class InnerType(value: String)

  case class Const1(value: String)

  case class Const2(id: Long, value: String)
  
  case class Const22(a: Int,
                     b: Int,
                     c: Int,
                     d: Int,
                     e: Int,
                     f: Int,
                     g: Int,
                     h: Int,
                     i: Int,
                     j: Int,
                     k: Int,
                     l: Int,
                     m: Int,
                     n: Int,
                     o: Int,
                     p: Int,
                     q: Int,
                     r: Int,
                     s: Int,
                     t: Int,
                     u: Int,
                     v: Int)

}