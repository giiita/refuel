package com.giitan.lang

import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util.TimeZone

import ScalaTime._
import com.giitan.injector.Injector
import org.scalatest.{Matchers, WordSpec}

class ScalaTimeTest extends WordSpec with Matchers with Injector {

  trait Context {
    protected val tz = new RuntimeTZ {
      override val TIME_ZONE: TimeZone = TimeZone.getTimeZone("Asia/Tokyo")
      override val ZONE_ID: ZoneId = ZoneId.of("Asia/Tokyo")
      override val ZONE_OFFSET: ZoneOffset = ZoneOffset.ofHours(9)
      override val DEFAULT_FORMAT: String = RuntimeTZ.DEFAULT_FORMAT
    }
    depends[RuntimeTZ](tz)
  }

  "ZonedDateTimeBs" should {
    "parse yyyy/MM/dd HH:mm:ss" in new Context {
      "2017/08/26 11:33:54".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
    "parse yyyyMMddHHmmss" in new Context {
      "20170826113354".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
    "parse yyyy/MM/dd" in new Context {
      "2017/08/26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyyMMdd" in new Context {
      "20170826".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy/M/d" in new Context {
      "2017/8/26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy/M/d H" in new Context {
      "2017/8/26 9".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 9, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-MM-dd HH:mm:ss" in new Context {
      "2017-08-26 11:33:54".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
    "parse yyyy-MM-dd" in new Context {
      "2017-08-26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-M-d" in new Context {
      "2017-8-26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-M-d H" in new Context {
      "2017-8-26 9".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 9, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-M-d HH" in new Context {
      "2017-8-26 09".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 9, 0, 0, 0, tz.ZONE_ID)
    }
    "parse unexpected" in new Context {
      try {
        "2017826".datetime
      } catch {
        case e: IllegalArgumentException => e.getMessage shouldBe "Unexpected datetime format. 2017826"
        case _ => fail()
      }
    }
  }

  "StringBs" should {
    "as default" in new Context {
      "2017/08/26 11:33:54".datetime.format() shouldBe "2017/8/26 11:33:54 +0900"
    }
    "as custom" in new Context {
      "2017/08/26 11:33:54".datetime.format("yyMd") shouldBe "17826"
    }

    "minToday" in new Context {
      "2017/08/26 11:33:54".datetime.minToday shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, RuntimeTZ.ZONE_ID)
    }

    "minTohour" in new Context {
      "2017/08/26 11:33:54".datetime.minTohour shouldBe ZonedDateTime.of(2017, 8, 26, 11, 0, 0, 0, RuntimeTZ.ZONE_ID)
    }

    "maxToday" in new Context {
      "2017/08/26 11:33:54".datetime.maxToday shouldBe ZonedDateTime.of(2017, 8, 26, 23, 59, 59, 0, RuntimeTZ.ZONE_ID)
    }

    "maxTohour" in new Context {
      "2017/08/26 11:33:54".datetime.maxTohour shouldBe ZonedDateTime.of(2017, 8, 26, 11, 59, 59, 0, RuntimeTZ.ZONE_ID)
    }

    "unixtime" in new Context {
      "2017/08/26 11:33:54".datetime.unixtime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, RuntimeTZ.ZONE_ID).toEpochSecond
    }
  }

  "UnixTimeBs" should {
    "as" in new Context {
      ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID).toEpochSecond.datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
  }

  "LocalDateTimeBs" should {
    "toEpochSec" in new Context {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54)
        .toEpochSec shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, ZoneId.of("Asia/Tokyo")).toEpochSecond
    }
    "format" in new Context {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54)
        .format() shouldBe "2017/8/26 11:33:54 +0900"
    }
    "toZonedDateTime" in new Context {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54)
        .toZonedDateTime shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, ZoneId.of("Asia/Tokyo"))
    }
  }
}
