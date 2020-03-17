package refuel.json.codecs.definition

import java.time.ZonedDateTime

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.builder.context.keylit.EndPointKeyLit
import refuel.json.entry.{JsAnyVal, JsNull, JsString}
import refuel.json.error.{DeserializeFailed, UnexpectedDeserializeType, UnsupportedOperation}
import refuel.json.{Codec, JsonVal}

import scala.util.{Failure, Success, Try}

private[codecs] trait AnyValCodecs {

  /**
   * The base of scala base api codec.
   *
   * @tparam T Target type param.
   */
  private[this] trait AnyValCodec[T] extends Codec[T] {
    def fail(bf: JsonVal, e: Throwable): DeserializeFailed

    def parse(bf: JsonVal): T

    override def serialize(t: T): JsonVal = JsAnyVal(t.toString)

    override final def deserialize(bf: JsonVal): Either[DeserializeFailed, T] = {
      Try {
        bf match {
          case JsNull => throw UnsupportedOperation("Null value cannot deserialize to String.")
          case _ => parse(bf)
        }
      } match {
        case Success(x) => Right(x)
        case Failure(x) => Left(fail(bf, x))
      }
    }

    override val keyLiteralRef: JsKeyLitOps = EndPointKeyLit
  }

  implicit final val IntCdc: Codec[Int] = new AnyValCodec[Int] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Int -> $bf", e)
    }

    def parse(bf: JsonVal): Int = Integer.parseInt(bf.toString)
  }

  implicit final val FloatCdc: Codec[Float] = new AnyValCodec[Float] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Float -> $bf", e)
    }

    def parse(bf: JsonVal): Float = java.lang.Float.parseFloat(bf.toString)
  }

  implicit final val DoubleCdc: Codec[Double] = new AnyValCodec[Double] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Double -> $bf", e)
    }

    def parse(bf: JsonVal): Double = java.lang.Double.parseDouble(bf.toString)
  }

  implicit final val LongCdc: Codec[Long] = new AnyValCodec[Long] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Long -> $bf", e)
    }

    def parse(bf: JsonVal): Long = java.lang.Long.parseLong(bf.toString)
  }

  implicit final val BooleanCdc: Codec[Boolean] = new AnyValCodec[Boolean] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Boolean -> $bf", e)
    }

    def parse(bf: JsonVal): Boolean = bf.toString.toLowerCase match {
      case "1" | "true" => true
      case "0" | "false" => false
      case _ => throw fail(bf, UnsupportedOperation("Only 1/0 or true/false can be boolean decoded"))
    }
  }

  implicit final val CharCdc: Codec[Char] = new AnyValCodec[Char] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Char -> $bf", e)
    }

    override def serialize(t: Char): JsonVal = JsString(t.toString)

    def parse(bf: JsonVal): Char = bf match {
      case JsAnyVal(x) => Integer.parseInt(x, 16).toChar
      case JsString(x) => x.head
      case _ => throw fail(bf, UnsupportedOperation("Only JsAnyVal or JsString can be charactor decoded"))
    }
  }

  implicit final val StringCdc: Codec[String] = new AnyValCodec[String] {

    override def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to String -> $bf", e)
    }

    override def serialize(t: String): JsonVal = JsString(t)

    def parse(bf: JsonVal): String = bf.toString
  }

  implicit final val ZonedDateTimeCdc: Codec[ZonedDateTime] = new Codec[ZonedDateTime] {

    import refuel.lang.ScalaTime._

    def fail(bf: JsonVal, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to ZonedDateTime -> $bf", e)
    }

    override def serialize(t: ZonedDateTime): JsonVal = JsString(t.format())

    override def deserialize(bf: JsonVal): Either[DeserializeFailed, ZonedDateTime] = {
      bf match {
        case x: JsAnyVal => LongCdc.deserialize(x).right.map(_.datetime)
        case JsString(x) => Right(x.datetime)
        case _ => Left(
          fail(bf, UnsupportedOperation("Only JsAnyVal or JsString can be charactor decoded"))
        )
      }
    }

    override val keyLiteralRef: JsKeyLitOps = EndPointKeyLit
  }
}
