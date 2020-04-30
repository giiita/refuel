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
      case JsString("cat")   => Cat("cat", json.named("beard").to[Int])
      case JsString("shark") => Shark("shark", json.named("filet").to[Int])
    }
  }

  def animalSerializer: Write[Animal] = Serialize {
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

  implicit def animalCodec: Codec[Animal] = Format(animalDeserializer)(animalSerializer)

  "Parsed by dynamic codec" should {
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
