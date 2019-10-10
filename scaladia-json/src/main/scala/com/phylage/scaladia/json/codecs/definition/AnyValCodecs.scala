package com.phylage.scaladia.json.codecs.definition

import java.time.ZonedDateTime

import com.phylage.scaladia.json.error.{DeserializeFailed, UnexpectedDeserializeType, UnsupportedOperation}
import com.phylage.scaladia.json.entry.{JsAnyVal, JsString}
import com.phylage.scaladia.json.{Codec, Json}

import scala.util.{Failure, Success, Try}

private[codecs] trait AnyValCodecs {

  /**
    * The base of scala base api codec.
    *
    * @tparam T Target type param.
    */
  private[this] trait AnyValCodec[T] extends Codec[T] {
    def fail(bf: Json, e: Throwable): DeserializeFailed

    def parse(bf: Json): T

    override def serialize(t: T): Json = JsAnyVal(t.toString)

    override final def deserialize(bf: Json): Either[DeserializeFailed, T] = {
      Try {
        parse(bf)
      } match {
        case Success(x) => Right(x)
        case Failure(x) => Left(fail(bf, x))
      }
    }
  }

  implicit final val IntCdc: Codec[Int] = new AnyValCodec[Int] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Int -> $bf", e)
    }

    def parse(bf: Json): Int = Integer.parseInt(bf.toString)
  }

  implicit final val FloatCdc: Codec[Float] = new AnyValCodec[Float] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Float -> $bf", e)
    }

    def parse(bf: Json): Float = java.lang.Float.parseFloat(bf.toString)
  }

  implicit final val DoubleCdc: Codec[Double] = new AnyValCodec[Double] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Double -> $bf", e)
    }

    def parse(bf: Json): Double = java.lang.Double.parseDouble(bf.toString)
  }

  implicit final val LongCdc: Codec[Long] = new AnyValCodec[Long] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Long -> $bf", e)
    }

    def parse(bf: Json): Long = java.lang.Long.parseLong(bf.toString)
  }

  implicit final val BooleanCdc: Codec[Boolean] = new AnyValCodec[Boolean] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Boolean -> $bf", e)
    }

    def parse(bf: Json): Boolean = bf.toString.toLowerCase match {
      case "1" | "true" => true
      case "0" | "false" => false
      case _             => throw fail(bf, UnsupportedOperation("Only 1/0 or true/false can be boolean decoded"))
    }
  }

  implicit final val CharCdc: Codec[Char] = new AnyValCodec[Char] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to Char -> $bf", e)
    }

    override def serialize(t: Char): Json = JsString(t.toString)

    def parse(bf: Json): Char = bf match {
      case JsAnyVal(x) => Integer.parseInt(x, 16).toChar
      case JsString(x) => x.head
      case _ => throw fail(bf, UnsupportedOperation("Only JsAnyVal or JsString can be charactor decoded"))
    }
  }

  implicit final val StringCdc: Codec[String] = new AnyValCodec[String] {

    override def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to String -> $bf", e)
    }

    override def serialize(t: String): Json = JsString(t)

    def parse(bf: Json): String = bf.unquote
  }

  implicit final val ZonedDateTimeCdc: Codec[ZonedDateTime] = new Codec[ZonedDateTime] {

    import com.phylage.scaladia.lang.ScalaTime._

    def fail(bf: Json, e: Throwable): DeserializeFailed = {
      UnexpectedDeserializeType(s"Cannot deserialize to ZonedDateTime -> $bf", e)
    }

    override def serialize(t: ZonedDateTime): Json = JsString(t.format())

    override def deserialize(bf: Json): Either[DeserializeFailed, ZonedDateTime] = {
      bf match {
        case x: JsAnyVal => LongCdc.deserialize(x).right.map(_.datetime)
        case JsString(x) => Right(x.datetime)
        case _ => Left(
          fail(bf, UnsupportedOperation("Only JsAnyVal or JsString can be charactor decoded"))
        )
      }
    }
  }
}
