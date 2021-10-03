package refuel.json.codecs.translation

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.inject.Injector
import refuel.json.codecs.{Codec, Write}
import refuel.json.{Json, JsonTransform}

class IterableCodecTranslatorTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  import JsonTransform._
  "Declarative codec definition translation" should {
    "case Seq" in {
      val row = Seq(Seq("foo", "bar"))
      val json = Json.arr(
        Json.arr("foo", "bar")
      )
      val codec = CodecOf.seq[Seq[String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

      val read = ReadOf.seq[Seq[String]]
      read.deserialize(json) shouldBe row


      val write = WriteOf.seq[Seq[String]]
      write.serialize(row) shouldBe json
    }

    "case List" in {
      val row = List(List("foo", "bar"))
      val json = Json.arr(
        Json.arr("foo", "bar")
      )
      val codec = CodecOf.list[List[String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

      val read = ReadOf.list(implicitly[Codec[List[String]]])
      read.deserialize(json) shouldBe row

      val write = WriteOf.list[List[String]]
      write.serialize(row) shouldBe json
    }

    "case Vector" in {
      val row = Vector(Vector("foo", "bar"))
      val json = Json.arr(
        Json.arr("foo", "bar")
      )
      val codec = CodecOf.vector[Vector[String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

      val read = ReadOf.vector(implicitly[Codec[Vector[String]]])
      read.deserialize(json) shouldBe row

      implicitly[Write[List[String]]]
      val write = WriteOf.vector[Vector[String]]
      write.serialize(row) shouldBe json
    }

    "case Set" in {
      val row = Set(Set("foo", "bar"))
      val json = Json.arr(
        Json.arr("foo", "bar")
      )
      val codec = CodecOf.set[Set[String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

      val read = ReadOf.set(implicitly[Codec[Set[String]]])
      read.deserialize(json) shouldBe row

      val write = WriteOf.set(implicitly[Codec[Set[String]]])
      write.serialize(row) shouldBe json
    }

    "case Array" in {
      val row = Array(Array("foo", "bar"))
      val json = Json.arr(
        Json.arr("foo", "bar")
      )
      val codec = CodecOf.array[Array[String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

//      val read = ReadOf.array(implicitly[Codec[Array[String]]])
//      read.deserialize(json) shouldBe row
//
//      val write = WriteOf.array(implicitly[Codec[Array[String]]])
      //      write.serialize(row) shouldBe json
    }

    "case Option" in {
      val row = Option(Option("foo"))
      val json = Json.str("foo")
      val codec = CodecOf.option[Option[String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

      val read = ReadOf.option[Option[String]](implicitly[Codec[Option[String]]])
      read.deserialize(json) shouldBe row

      val write = WriteOf.option[Option[String]](implicitly[Codec[Option[String]]])
      write.serialize(row) shouldBe json
    }

    "case Map" in {
      val row = Map("foo" -> Map("bar" -> "value"))
      val json = Json.obj("foo" -> Json.obj("bar" -> "value"))
      val codec = CodecOf.map[String, Map[String, String]]
      codec.deserialize(json) shouldBe row
      codec.serialize(row) shouldBe json

      val read = ReadOf.map[String, Map[String, String]]
      read.deserialize(json) shouldBe row

      val write = WriteOf.map[String, Map[String, String]]
      write.serialize(row) shouldBe json
    }
  }
}
