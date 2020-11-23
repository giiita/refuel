package refuel.json

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.codecs.{Read, Write}
import refuel.json.entry.JsString

class CodecDefTest extends AsyncWordSpec with Matchers with Diagrams with JsonTransform with CodecDef {

  sealed abstract class Animal(name: String)

  case class Cat(name: String, beard: Int = 6) extends Animal(name)

  case class Shark(name: String, filet: Int = 4) extends Animal(name)

  def animalDeserializer: Read[Animal] = Deserialize { json =>
    json.named("name") match {
      case JsString("cat")   => Cat("cat", json.named("beard").des[Int])
      case JsString("shark") => Shark("shark", json.named("filet").des[Int])
    }
  }

  implicit def animalSerializer: Write[Animal] = Serialize {
    case Cat(a, b) =>
      Json.obj(
        "name"  -> a,
        "beard" -> b
      )
    case Shark(a, b) =>
      Json.obj(
        "name"  -> a,
        "filet" -> b
      )
  }

  // Auto complete compile test
  {
    Json.obj(
      "boo" -> (Cat("tama"): Animal)
    )
    Cat("tama").ser(CaseClassCodec.from[Cat])
  }

  implicit def animalCodec: Codec[Animal] = Format(animalDeserializer.deserialize)(animalSerializer.serialize)

  "Parsed by dynamic codec" should {
    "JsArray dig" in {
      val json = s"""{
                    | "entries": [
                    |    {
                    |      "id": "aaa",
                    |      "value": "foo"
                    |    },
                    |    {
                    |      "id": "bbb",
                    |      "value": "bar"
                    |    }
                    |  ]
                    |}""".stripMargin.jsonTree
      ("entries" @@ "id").dig(json).des[Seq[String]] shouldBe Seq("aaa", "bbb")
    }
    "cat" in {
      val input = Cat("cat", 10)
      input.toJString(animalCodec).as[Animal] shouldBe Right(input)
    }
    "shark" in {
      val input = Shark("shark", 10)
      input.toJString(animalCodec).as[Animal] shouldBe Right(input)
    }
  }

  "Standard support types are implicitly resolved without assigning Codec" should {
    "Can compile" in {
      Seq(
        Cat("foo"),
        Cat("bar")
      ).toJString[Seq[Animal]] shouldBe """[{"name":"foo","beard":6},{"name":"bar","beard":6}]"""
    }
  }

}

case class AdFormatRegReads(charCountLimit: Int, restrictedSymbol: List[String])
