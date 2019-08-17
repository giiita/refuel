package com.phylage.scaladia.lang

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, LocalTime, ZonedDateTime}

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.lang.period.{EpochDateTime, FromTo}

object ScalaTime extends Injector {
  /**
    * If not setting auto injectable RuntimeTz,
    * use default RuntimeTZ.
    */
  private[this] val TZ = inject[RuntimeTZ]

  /**
    * Get a current time.
    *
    * {{{
    * import com.phylage.scaladia.lang.ScalaTime._
    * val currentTime = now
    * }}}
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
    def epoch: Long = value.toEpochSecond(TZ.ZONE_OFFSET)

    /**
      * Create period object based on this date.
      *
      * {{{
      *   import com.phylage.scaladia.lang.ScalaTime._
      *
      *   "2019-08-05 10:00:00".datetime.periodWith(_.plusDays(6).maxToday)(DeliveryPeriod)
      *   == DeliveryPeriod("2019-08-05 10:00:00".datetime.epoch, "2019-08-11 23:59:59".datetime.epoch)
      *
      *   "2019-08-05 10:00:00".datetime.periodWith(_.minusDays(6).minToday)(DeliveryPeriod)
      *   == DeliveryPeriod("2019-07-31 00:00:00".datetime.epoch, "2019-08-05 10:00:00".datetime.epoch)
      * }}}
      *
      * @param anotherTimeApplyment How long to create a period from this date.
      * @param contruct             FromTo subtype constructor.
      * @tparam T Period type.
      * @return
      */
    def periodWith[T <: FromTo](anotherTimeApplyment: LocalDateTime => LocalDateTime)
                               (contruct: (EpochDateTime, EpochDateTime) => T): T = {
      anotherTimeApplyment(value) match {
        case x if x.isAfter(value) => contruct(value, x)
        case x => contruct(x, value)
      }
    }

    /**
      * LocalDateTime to this day, 00:00:00.0
      *
      * @return
      */
    def minToday: LocalDateTime = value.`with`(LocalTime.MIN)

    /**
      * LocalDateTime to this hour, HH:00:00.0
      *
      * @return
      */
    def minTohour: LocalDateTime = value.`with`(LocalTime.MIN.withHour(value.getHour))

    /**
      * LocalDateTime to this day, 23:59:59.99999999
      *
      * @return
      */
    def maxToday: LocalDateTime = value.`with`(LocalTime.MAX)

    /**
      * LocalDateTime to this hour, HH:59:59.99999999
      *
      * @return
      */
    def maxTohour: LocalDateTime = value.`with`(LocalTime.MAX.withHour(value.getHour))

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
      * Create period object based on this date.
      *
      * {{{
      *   import com.phylage.scaladia.lang.ScalaTime._
      *
      *   "2019-08-05 10:00:00".datetime.periodWith(_.plusDays(6).maxToday)(DeliveryPeriod)
      *   == DeliveryPeriod("2019-08-05 10:00:00".datetime.epoch, "2019-08-11 23:59:59".datetime.epoch)
      *
      *   "2019-08-05 10:00:00".datetime.periodWith(_.minusDays(6).minToday)(DeliveryPeriod)
      *   == DeliveryPeriod("2019-07-31 00:00:00".datetime.epoch, "2019-08-05 10:00:00".datetime.epoch)
      * }}}
      *
      * @param anotherTimeApplyment How long to create a period from this date.
      * @param contruct             FromTo subtype constructor.
      * @tparam T Period type.
      * @return
      */
    def periodWith[T <: FromTo](anotherTimeApplyment: ZonedDateTime => ZonedDateTime)
                               (contruct: (EpochDateTime, EpochDateTime) => T): T = {
      anotherTimeApplyment(value) match {
        case x if x.isAfter(value) => contruct(value, x)
        case x => contruct(x, value)
      }
    }

    /**
      * ZonedDateTime convert to string.
      *
      * @param format customized format: default "yyyy/MM/dd HH:mm:ss"
      * @return
      */
    def formatTo(format: String = TZ.DEFAULT_FORMAT): String = DateTimeFormatter.ofPattern(format).format(value)

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
    def minTohour: ZonedDateTime = value.`with`(LocalTime.MIN.withHour(value.getHour))

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
    def maxTohour: ZonedDateTime = value.`with`(LocalTime.MAX.withHour(value.getHour))

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
