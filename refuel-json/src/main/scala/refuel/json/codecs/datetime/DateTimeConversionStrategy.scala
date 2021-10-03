package refuel.json.codecs.datetime

import refuel.json.{Json, JsonVal}
import refuel.lang.RuntimeTZ

import java.time.ZonedDateTime
import scala.util.{Success, Try}

sealed trait DateTimeConversionStrategy {
  def serialize(date: ZonedDateTime)(implicit st: RuntimeTZ): Try[JsonVal]
  def deserialize(json: JsonVal)(implicit st: RuntimeTZ): Try[ZonedDateTime]
}

object DateTimeConversionStrategy {
  object Epoch extends DateTimeConversionStrategy {
    override def serialize(date: ZonedDateTime)(implicit st: RuntimeTZ): Try[JsonVal] = {
      Success(
        Json.any(date.toEpochSecond)
      )
    }

    override def deserialize(json: JsonVal)(implicit st: RuntimeTZ): Try[ZonedDateTime] = {
      Try {
        ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(java.lang.Long.parseLong(json.pure)), st.ZoneId)
      }
    }
  }
  object EpochMillis extends DateTimeConversionStrategy {
    override def serialize(date: ZonedDateTime)(implicit st: RuntimeTZ): Try[JsonVal] = {
      Success(
        Json.any(date.toInstant.toEpochMilli)
      )
    }

    override def deserialize(json: JsonVal)(implicit st: RuntimeTZ): Try[ZonedDateTime] = {
      Try {
        ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(java.lang.Long.parseLong(json.pure)), st.ZoneId)
      }
    }
  }
  object Formatter extends DateTimeConversionStrategy {
    override def serialize(date: ZonedDateTime)(implicit st: RuntimeTZ): Try[JsonVal] = {
      Success(
        Json.str(date.format(st.Format))
      )
    }

    override def deserialize(json: JsonVal)(implicit st: RuntimeTZ): Try[ZonedDateTime] = {
      Try {
        ZonedDateTime.parse(json.pure, st.Format)
      }
    }
  }
}
