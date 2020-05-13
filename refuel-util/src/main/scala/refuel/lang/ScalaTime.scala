package refuel.lang

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, LocalTime, ZonedDateTime}

import refuel.injector.{AutoInject, Injector}

@deprecated("Instead, use dependency injection")
object ScalaTime extends ScalaTime(RuntimeTZ)

/** Use DI as a starting point.
  * ```
  * MyService(st: ScalaTime) {
  *   import st._
  *
  *     "2020-01-01".datetime
  * }
  * ```
  * By default, the system default TimeZone is used.
  * I would override it as needed and refer to it by mixing in the AutoInject.
  */
class ScalaTime(TZ: RuntimeTZ) extends Injector with AutoInject {

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
      ScalaTimeSupport.DATE_TIME_PATTERN_SET
        .find(x => value.matches(x.regex))
        .fold {
          throw new IllegalArgumentException(
            s"Unexpected datetime format. $value"
          )
        } { x =>
          ZonedDateTime.of(
            LocalDateTime
              .parse(x.normalize(value), ScalaTimeSupport.convertWith),
            TZ.ZONE_ID
          )
        }
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

    /**
      * Convert to ZonedDateTime with default zone id.
      *
      * @return
      */
    def toZonedDateTime: ZonedDateTime = ZonedDateTime.of(value, TZ.ZONE_ID)
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
    def milliToDatetime: ZonedDateTime =
      ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), TZ.ZONE_ID)
  }

}

object ScalaTimeSupport {
  val convertWith: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy/M/d H:m:s")

  val DATE_TIME_PATTERN_SET: Seq[DateFormattedPattern] = Seq(
    PatternWithinSymbol,
    PatternWithoutSymbol,
    PatternOnlyDateWithinSymbol,
    PatternOnlyDateWithoutSymbol,
    PatternUpToHourWithinSymbol
  )

  trait DateFormattedPattern {
    val regex: String
    val normalize: String => String
  }

  private case object PatternWithinSymbol extends DateFormattedPattern {
    val regex: String =
      s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})\\s+([\\d]{1,2}):([\\d]{1,2}):([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:$5:$6")
  }

  private case object PatternWithoutSymbol extends DateFormattedPattern {
    val regex: String =
      "^([\\d]{4})([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{2})$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:$5:$6")
  }

  private case object PatternOnlyDateWithinSymbol extends DateFormattedPattern {
    val regex: String               = s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 00:00:00")
  }

  private case object PatternOnlyDateWithoutSymbol extends DateFormattedPattern {
    val regex: String               = "^([\\d]{4})([\\d]{2})([\\d]{2})$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 00:00:00")
  }

  private case object PatternUpToHourWithinSymbol extends DateFormattedPattern {
    val regex: String =
      s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})\\s+([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:00:00")
  }

}
