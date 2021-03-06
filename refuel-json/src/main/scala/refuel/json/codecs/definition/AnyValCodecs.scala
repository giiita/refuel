package refuel.json.codecs.definition

import java.time.ZonedDateTime

import refuel.json.entry.{JsAnyVal, JsEmpty, JsNull, JsString}
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializeType, UnsupportedOperation}
import refuel.json.{Codec, JsonVal}
import refuel.lang.ScalaTime

import scala.util.{Failure, Success, Try}

private[codecs] trait AnyValCodecs {

  implicit final val UnitCdc: Codec[Unit] = new Codec[Unit] {
    override def serialize(t: Unit): JsonVal    = JsEmpty
    override def deserialize(bf: JsonVal): Unit = ()
  }

  /**
    * The base of scala base api codec.
    *
    * @tparam T Target type param.
    */
  private[this] trait AnyValCodec[T] extends Codec[T] {
    def fail(bf: JsonVal, e: Throwable): DeserializeFailed

    def parse(bf: JsonVal): T

    override def serialize(t: T): JsonVal = JsAnyVal(t.toString)

    override final def deserialize(bf: JsonVal): T = {
      Try {
        bf match {
          case JsNull => throw UnsupportedOperation("Null value cannot deserialize.")
          case _      => parse(bf)
        }
      } match {
        case Success(x) => x
        case Failure(x) => throw fail(bf, x)
      }
    }
  }

  implicit final val IntCdc: Codec[Int] = new AnyValCodec[Int] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into an Int", e)
    }

    def parse(bf: JsonVal): Int = Integer.parseInt(bf.pure)
  }

  implicit final val FloatCdc: Codec[Float] = new AnyValCodec[Float] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into a Float", e)
    }

    def parse(bf: JsonVal): Float = java.lang.Float.parseFloat(bf.pure)
  }

  implicit final val DoubleCdc: Codec[Double] = new AnyValCodec[Double] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into a Double", e)
    }

    def parse(bf: JsonVal): Double = java.lang.Double.parseDouble(bf.pure)
  }

  implicit final val LongCdc: Codec[Long] = new AnyValCodec[Long] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into a Long", e)
    }

    def parse(bf: JsonVal): Long = java.lang.Long.parseLong(bf.pure)
  }

  implicit final val BooleanCdc: Codec[Boolean] = new AnyValCodec[Boolean] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into a Boolean", e)
    }

    def parse(bf: JsonVal): Boolean = bf.pure.toLowerCase match {
      case "1" | "true"  => true
      case "0" | "false" => false
      case _             => throw fail(bf, UnsupportedOperation("Only 1/0 or true/false can be boolean decoded"))
    }
  }

  implicit final val CharCdc: Codec[Char] = new AnyValCodec[Char] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into a Char", e)
    }

    override def serialize(t: Char): JsonVal = JsString(t.toString)

    def parse(bf: JsonVal): Char = bf match {
      case JsAnyVal(x) => Integer.parseInt(x, 16).toChar
      case JsString(x) => x.head
      case _           => throw fail(bf, UnsupportedOperation("Only JsAnyVal or JsString can be charactor decoded"))
    }
  }

  implicit final val StringCdc: Codec[String] = new AnyValCodec[String] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize $bf into a String", e)
    }

    override def serialize(t: String): JsonVal = JsString(t)

    def parse(bf: JsonVal): String = bf.pure
  }

  implicit def ZonedDateTimeCdc(
      implicit st: ScalaTime,
      strategy: DateTimeParserStrategy = DateTimeParserStrategy.Formatter
  ): Codec[ZonedDateTime] =
    new Codec[ZonedDateTime] {

      override def serialize(t: ZonedDateTime): JsonVal = strategy.serialize(t)

      override def deserialize(bf: JsonVal): ZonedDateTime = {
        strategy.deserialize(bf)
      }
    }
}
