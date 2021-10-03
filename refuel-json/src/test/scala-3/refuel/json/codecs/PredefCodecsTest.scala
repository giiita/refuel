package refuel.json.codecs

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.inject.Injector
import refuel.json.codecs.datetime.DateTimeConversionStrategy
import refuel.json.exception.IllegalJsonFormatException
import refuel.json.{Json, JsonTransform, JsonVal}
import refuel.lang.RuntimeTZ

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import scala.util.{Failure, Success, Try}

class PredefCodecsTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  import JsonTransform.{*, given}

  case class TestProduct(name: String, value: Int)
  import scala.language.implicitConversions
  "Derivings compitation" should {
    "Can derive Codec" in {
      Derive[Map[String, Option[List[Seq[Vector[Array[Set[(String, String)]]]]]]]]
      succeed
    }
  }

  "Primitive types codec auto generation" should {
    "str rw" in {
      val row = "foo"
      val tree = Json.str("foo")
      tree.readAs[String].fold(fail(_), _ shouldBe row)
      row.writeAs[String].fold(fail(_), _ shouldBe tree)
    }

    "char rw" in {
      val row = 'r'
      val tree = Json.str("r")
      tree.readAs[Char].fold(fail(_), _ shouldBe row)
      row.writeAs[Char].fold(fail(_), _ shouldBe tree)
    }

    "int rw" in {
      val row = 3
      val tree = Json.any(3)
      type tpe = Int
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "long rw" in {
      val row = 3L
      val tree = Json.any(3L)
      type tpe = Long
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "short rw" in {
      val row = 3.toShort
      val tree = Json.any(3)
      type tpe = Short
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "double rw" in {
      val row = 3.2
      val tree = Json.any(3.2)
      type tpe = Double
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "float rw" in {
      val row = 3.2f
      val tree = Json.any(3.2f)
      type tpe = Float
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "unit rw" in {
      val row = ()
      val tree = Json.Empty
      type tpe = Unit
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Bool rw" in {
      val row = true
      val tree = Json.any("true")
      type tpe = Boolean
      Json.any("true").readAs[tpe].fold(fail(_), _ shouldBe row)
      Json.any("1").readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }
  }

  "Reserved type codec auto translation" {
    "json rw" in {
      val row = Json.obj("foo" -> "bar")
      val tree = Json.obj("foo" -> "bar")
      type tpe = JsonVal
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "zoned date time rw with epoch" in {
      given RuntimeTZ = inject[RuntimeTZ]
      given DateTimeConversionStrategy = DateTimeConversionStrategy.Epoch
      val row = ZonedDateTime.of(2021, 10, 1, 10, 0, 0, 0, given_RuntimeTZ.ZoneId)
      val tree = Json.any(row.toEpochSecond)
      type tpe = ZonedDateTime
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "zoned date time rw with epoch millis" in {
      given RuntimeTZ = inject[RuntimeTZ]
      given DateTimeConversionStrategy = DateTimeConversionStrategy.EpochMillis
      val row = {
        val date = ZonedDateTime.now(given_RuntimeTZ.ZoneId)
        date.withNano(0)
      }
      val tree = Json.any(row.toInstant.toEpochMilli)
      type tpe = ZonedDateTime
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "zoned date time rw with format" in {
      given RuntimeTZ = inject[RuntimeTZ]
      given DateTimeConversionStrategy = DateTimeConversionStrategy.Formatter
      val row = ZonedDateTime.of(2021, 10, 1, 10, 0, 0, 0, given_RuntimeTZ.ZoneId)
      val tree = Json.str(row.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
      type tpe = ZonedDateTime
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Try (Success) rw" in {
      val row = 3.2f
      val tree = Json.any(3.2f)
      type tpe = Try[Float]
      tree.readAs[tpe].fold(fail(_), _ shouldBe Success(row))
      Success(row).writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Try (Failure) rw" in {
      val row = 3.2f
      val tree = Json.any(3.2f)
      type tpe = Try[Boolean]
      tree.readAs[tpe].fold(fail(_), _ shouldBe Failure(new IllegalJsonFormatException("Cannot deserialize 3.2 into a Boolean. Only 1/0 or true/false can be boolean decoded.")))
      Failure[Boolean](new Exception("test")).writeAs[tpe]
      succeed
    }

    "Set rw" in {
      val row = Set("foo", "bar")
      val tree = Json.arr("foo", "bar")
      type tpe = Set[String]
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Seq rw" in {
      val row = Seq("foo", "foo", "bar")
      val tree = Json.arr("foo", "foo", "bar")
      type tpe = Seq[String]
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "List rw" in {
      val row = List("foo", "foo", "bar")
      val tree = Json.arr("foo", "foo", "bar")
      type tpe = List[String]
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Vector rw" in {
      val row = Vector("foo", "foo", "bar")
      val tree = Json.arr("foo", "foo", "bar")
      type tpe = Vector[String]
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Array rw" in {
      val row = Array("foo", "foo", "bar")
      val tree = Json.arr("foo", "foo", "bar")
      type tpe = Array[String]
      tree.readAs[tpe].fold(fail(_), _ shouldBe row)
      row.writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Either (Right) rw" in {
      val row = 3.2f
      val tree = Json.any(3.2f)
      type tpe = Either[String, Float]
      tree.readAs[tpe].fold(fail(_), _ shouldBe Right(row))
      Right(row).writeAs[tpe].fold(fail(_), _ shouldBe tree)
    }

    "Either (Left) rw" in {
      val row = 3.2f
      val tree = Json.any(3.2f)
      type tpe = Either[Float, Boolean]
      tree.readAs[tpe].fold(fail(_), _ shouldBe Left(row))
      Left[Float, Boolean](row).writeAs[tpe]
      succeed
    }
  }
}
