package refuel.lang

import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject, Injector}

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, LocalTime, ZonedDateTime}

/** Use DI as a starting point.
  * ```
  * MyService(st: ScalaTime) {
  *   import st._
  *   "2020-01-01".datetime
  * }
  * ```
  * By default, the system default TimeZone is used.
  * I would override it as needed and refer to it by mixing in the AutoInject.
  */
@Inject[Finally]
class ScalaTimeImpl(TZ: RuntimeTZ, sts: ScalaTimeSupport) extends ScalaTime(TZ, sts) with AutoInject

abstract class ScalaTime(TZ: RuntimeTZ, sts: ScalaTimeSupport) extends Injector {

  /**
    * Get a current time.
    *
    * {{{
    * import refuel.lang.ScalaTime._
    * val currentTime = now
    * }}}
    *
    * @return
    */
  def now: ZonedDateTime = ZonedDateTime.now(TZ.ZONE_ID)

  implicit class StringBs(value: String) {
    def epochtime: ZonedDateTime = {
      value.toLong.datetime
    }

    /**
      * Convert from arbitrary date string to ZonedDateTime.
      * Supported type is,
      *
      * <ul>
      * <li>yyyy/M/d H:m:s</li>
      * <li>yyyy/M/d</li>
      * <li>yyyy/M/d H</li>
      * <li>yyyyMMdd</li>
      * <li>yyyyMMddHHmmss</li>
      * <li>yyyy-M-d H:m:s</li>
      * <li>yyyy-M-d</li>
      * <li>yyyy-M-d H</li>
      * </ul>
      *
      * @return
      */
    def datetime: ZonedDateTime = {
      ZonedDateTime.of(
        sts.toLocalDateTime(value),
        TZ.ZONE_ID
      )
    }
  }

  implicit class LocalDateTimeBs(value: LocalDateTime) {

    /**
      * Convert to epoch second.
      *
      * @return
      */
    def epoch: Long = value.toEpochSecond(TZ.ZONE_OFFSET)

    /**
      * LocalDateTime to this day, 00:00:00.0
      *
      * @return
      */
    def minToday: LocalDateTime = value.toZonedDateTime.minToday.toLocalDateTime

    /**
      * LocalDateTime to this hour, HH:00:00.0
      *
      * @return
      */
    def minTohour: LocalDateTime =
      value.toZonedDateTime.minTohour.toLocalDateTime

    /**
      * Convert to ZonedDateTime with default zone id.
      *
      * @return
      */
    def toZonedDateTime: ZonedDateTime = ZonedDateTime.of(value, TZ.ZONE_ID)

    /**
      * LocalDateTime to this day, 23:59:59.99999999
      *
      * @return
      */
    def maxToday: LocalDateTime = value.toZonedDateTime.maxToday.toLocalDateTime

    /**
      * LocalDateTime to this hour, HH:59:59.99999999
      *
      * @return
      */
    def maxTohour: LocalDateTime =
      value.toZonedDateTime.maxTohour.toLocalDateTime

    /**
      * ZonedDateTime convert to string with default date formatter.
      *
      * @return
      */
    def format(): String = value.toZonedDateTime.format(TZ.format)
  }

  implicit class ZonedDateTimeBs(value: ZonedDateTime) {

    /**
      * ZonedDateTime convert to string with default date formatter.
      *
      * @return
      */
    def format(): String = value.format(TZ.format)

    /**
      * ZonedDateTime convert to string.
      *
      * @param format customized format: default "yyyy/MM/dd HH:mm:ss"
      * @return
      */
    def formatTo(format: String = TZ.DEFAULT_FORMAT): String =
      DateTimeFormatter.ofPattern(format).format(value)

    /**
      * ZonedDateTime to this day, 00:00:00.0
      *
      * @return
      */
    def minToday: ZonedDateTime = value.`with`(LocalTime.MIN)

    /**
      * ZonedDateTime to this hour, HH:00:00.0
      *
      * @return
      */
    def minTohour: ZonedDateTime =
      value.`with`(LocalTime.MIN.withHour(value.getHour))

    /**
      * ZonedDateTime to this day, 23:59:59.99999999
      *
      * @return
      */
    def maxToday: ZonedDateTime = value.`with`(LocalTime.MAX)

    /**
      * ZonedDateTime to this hour, HH:59:59.99999999
      *
      * @return
      */
    def maxTohour: ZonedDateTime =
      value.`with`(LocalTime.MAX.withHour(value.getHour))

    /**
      * ZonedDateTime to unixtime
      *
      * @return
      */
    def epoch: Long = value.toEpochSecond

    def epochMillis: Long = value.toInstant.toEpochMilli
  }

  implicit class UnixTimeBs(value: Long) {

    /**
      * Unixtime to ZonedDateTime.
      *
      * @return
      */
    def datetime: ZonedDateTime =
      ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), TZ.ZONE_ID)

    /**
      * Unixtime to ZonedDateTime.
      *
      * @return
      */
    def millisToDatetime: ZonedDateTime =
      ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), TZ.ZONE_ID)
  }

}

@Inject[Finally]
class ScalaTimeSupport(patterns: Iterable[DateFormattedPattern]) extends AutoInject {
  val DATE_TIME_PATTERN_SET: Seq[DateFormattedPattern] = Seq(
    ScalaTimeSupport.PatternWithinSymbol,
    ScalaTimeSupport.PatternWithoutSymbol,
    ScalaTimeSupport.PatternOnlyDateWithinSymbol,
    ScalaTimeSupport.PatternOnlyDateWithoutSymbol,
    ScalaTimeSupport.PatternUpToHourWithinSymbol
  )
  val convertWith: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy/M/d H:m:s")

  private[refuel] def toLocalDateTime(value: String): LocalDateTime = {
    println(patterns)
    patterns
      .foldLeft[Option[LocalDateTime]](None) { (result, next) =>
        result.orElse {
          next.toLocalDate(value)
        }
      }
      .getOrElse {
        throw new IllegalArgumentException(
          s"Unexpected datetime format. $value"
        )
      }
  }
}

trait DateFormattedPattern {
  def toLocalDate(value: String): Option[LocalDateTime]
}

object ScalaTimeSupport {

  val convertWith: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy/M/d H:m:s")

  trait RegexDateFormattedPattern extends DateFormattedPattern {
    val regex: String
    val normalize: String => String
    def toLocalDate(value: String): Option[LocalDateTime] =
      if (value.matches(regex)) {
        Some(
          LocalDateTime
            .parse(normalize(value), convertWith)
        )
      } else None
  }

  @Inject[Finally]
  case object PatternWithinSymbol extends RegexDateFormattedPattern with AutoInject {
    val regex: String =
      s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})\\s+([\\d]{1,2}):([\\d]{1,2}):([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:$5:$6")
  }

  @Inject[Finally]
  case object PatternWithoutSymbol extends RegexDateFormattedPattern with AutoInject {
    val regex: String =
      "^([\\d]{4})([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{2})$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:$5:$6")
  }

  @Inject[Finally]
  case object PatternOnlyDateWithinSymbol extends RegexDateFormattedPattern with AutoInject {
    val regex: String               = s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 00:00:00")
  }

  @Inject[Finally]
  case object PatternOnlyDateWithoutSymbol extends RegexDateFormattedPattern with AutoInject {
    val regex: String               = "^([\\d]{4})([\\d]{2})([\\d]{2})$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 00:00:00")
  }

  @Inject[Finally]
  case object PatternUpToHourWithinSymbol extends RegexDateFormattedPattern with AutoInject {
    val regex: String =
      s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})\\s+([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:00:00")
  }
}
