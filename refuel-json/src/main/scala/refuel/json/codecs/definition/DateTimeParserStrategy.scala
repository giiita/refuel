package refuel.json.codecs.definition

import java.time.ZonedDateTime

import refuel.json.{CodecDef, Json, JsonVal}
import refuel.lang.ScalaTime

sealed trait DateTimeParserStrategy {
  def serialize(date: ZonedDateTime)(implicit st: ScalaTime): JsonVal
  def deserialize(json: JsonVal)(implicit st: ScalaTime): ZonedDateTime
}

object DateTimeParserStrategy extends CodecDef {
  object Epoch extends DateTimeParserStrategy {
    override def serialize(date: ZonedDateTime)(implicit st: ScalaTime): JsonVal = {
      Json.any(date.toEpochSecond)
    }

    override def deserialize(json: JsonVal)(implicit st: ScalaTime): ZonedDateTime = {
      import st._
      json.des[Long].datetime
    }
  }
  object EpochMillis extends DateTimeParserStrategy {
    override def serialize(date: ZonedDateTime)(implicit st: ScalaTime): JsonVal = {
      import st._
      Json.any(date.epochMillis)
    }

    override def deserialize(json: JsonVal)(implicit st: ScalaTime): ZonedDateTime = {
      import st._
      json.des[Long].millisToDatetime
    }
  }
  object Formatter extends DateTimeParserStrategy {
    override def serialize(date: ZonedDateTime)(implicit st: ScalaTime): JsonVal = {
      import st._
      date.format()
    }

    override def deserialize(json: JsonVal)(implicit st: ScalaTime): ZonedDateTime = {
      import st._
      json.des[String].datetime
    }
  }
}
