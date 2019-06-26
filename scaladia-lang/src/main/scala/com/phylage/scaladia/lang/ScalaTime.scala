package com.phylage.scaladia.lang

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZonedDateTime}

import com.phylage.scaladia.injector.Injector

import scala.util.matching.Regex

object ScalaTime extends Injector {
  type DefaultDateType = ZonedDateTime

  /**
    * If not setting auto injectable RuntimeTz,
    * use default RuntimeTZ
    */
  private val TZ = inject[RuntimeTZ]

  /**
    * Get a current time.
    *
    * <pre>
    * import jupiter.common.util.JupiterTime._
    * val currentTime = now
    * <pre>
    *
    * @return
    */
  def now: ZonedDateTime = ZonedDateTime.now()

  implicit class StringBs(value: String) {
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
      ScalaTimeSupport.DATE_TIME_PATTERN_SET.find(x => value.matches(x.regex)).map { x =>
        x.normalize(value)
        ZonedDateTime.of(LocalDateTime.parse(x.normalize(value), ScalaTimeSupport.convertWith), TZ.ZONE_ID)
      } getOrElse (throw new IllegalArgumentException(s"Unexpected datetime format. $value"))
    }
  }

  implicit class LocalDateTimeBs(value: LocalDateTime) {
    /**
      * Convert to epoch second.
      *
      * @return
      */
    def toEpochSec: Long = value.toEpochSecond(TZ.ZONE_OFFSET)

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
    def format: String = value.format(TZ.format)

    /**
      * ZonedDateTime convert to string.
      *
      * @param format customized format: default "yyyy/MM/dd HH:mm:ss"
      * @return
      */
    def format(format: String = TZ.DEFAULT_FORMAT): String = DateTimeFormatter.ofPattern(format).format(value)

    /**
      * ZonedDateTime to this day, 00:00:00
      *
      * @return
      */
    def minToday: ZonedDateTime = value.format("yyyy/MM/dd 00:00:00").datetime

    /**
      * ZonedDateTime to this hour, HH:00:00
      *
      * @return
      */
    def minTohour: ZonedDateTime = value.format("yyyy/MM/dd HH:00:00").datetime

    /**
      * ZonedDateTime to this day, 23:59:59
      *
      * @return
      */
    def maxToday: ZonedDateTime = value.format("yyyy/MM/dd 23:59:59").datetime

    /**
      * ZonedDateTime to this hour, HH:59:59
      *
      * @return
      */
    def maxTohour: ZonedDateTime = value.format("yyyy/MM/dd HH:59:59").datetime

    /**
      * ZonedDateTime to unixtime
      *
      * @return
      */
    def unixtime: Long = value.toEpochSecond
  }

  implicit class UnixTimeBs(value: Long) {
    /**
      * Unixtime to ZonedDateTime.
      *
      * @return
      */
    def datetime: ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), TZ.ZONE_ID)
  }

}

object ScalaTimeSupport {
  val convertWith: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m:s")

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
    val regex: String = s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})\\s+([\\d]{1,2}):([\\d]{1,2}):([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:$5:$6")
  }

  private case object PatternWithoutSymbol extends DateFormattedPattern {
    val regex: String = "^([\\d]{4})([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{2})([\\d]{2})$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:$5:$6")
  }

  private case object PatternOnlyDateWithinSymbol extends DateFormattedPattern {
    val regex: String = s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 00:00:00")
  }

  private case object PatternOnlyDateWithoutSymbol extends DateFormattedPattern {
    val regex: String = "^([\\d]{4})([\\d]{2})([\\d]{2})$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 00:00:00")
  }

  private case object PatternUpToHourWithinSymbol extends DateFormattedPattern {
    val regex: String = s"^([\\d]{4})[/|-]{1}([\\d]{1,2})[/|-]{1}([\\d]{1,2})\\s+([\\d]{1,2})$$"
    val normalize: String => String = _.replaceAll(regex, "$1/$2/$3 $4:00:00")
  }

}