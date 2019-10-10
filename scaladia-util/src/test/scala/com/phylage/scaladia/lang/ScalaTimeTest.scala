package com.phylage.scaladia.lang

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

import com.phylage.scaladia.injector.Injector
import com.phylage.scaladia.lang.ScalaTime._
import com.phylage.scaladia.lang.period.{EpochDateTime, FromTo}
import org.scalatest.{Matchers, WordSpec}

import scala.util.{Failure, Try}

class ScalaTimeTest extends WordSpec with Matchers with Injector {

  trait Context {
    val tz = inject[RuntimeTZ]
  }

  "ZonedDateTimeBs" should {
    "periodWith - before case" in new Context {

      case class LocalPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo

      "2018/10/01 11:05:11".datetime.periodWith(_.minusDays(2).minToday)(LocalPeriod) shouldBe
        LocalPeriod(
          "2018/9/29 00:00:00".datetime.epoch,
          "2018/10/01 11:05:11".datetime.toEpochSecond
        )
    }

    "periodWith - after case" in new Context {

      case class LocalPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo

      "2018/10/01".datetime.periodWith(_.plusDays(2).maxToday)(LocalPeriod) shouldBe
        LocalPeriod(
          "2018/10/01".datetime.epoch,
          ZonedDateTime.of(2018, 10, 3, 23, 59, 59, 999999999, tz.ZONE_ID).epoch
        )
    }

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
      Try {
        "2017826".datetime
      } match {
        case Failure(e: IllegalArgumentException) => e.getMessage shouldBe "Unexpected datetime format. 2017826"
        case _ => fail()
      }
    }
  }

  "StringBs" should {
    "as default" in new Context {
      "2017/08/26 11:33:54".datetime.format() shouldBe "2017/8/26 11:33:54"
    }
    "as custom" in new Context {
      "2017/08/26 11:33:54".datetime.formatTo("yyMd") shouldBe "17826"
    }

    "minToday" in new Context {
      "2017/08/26 11:33:54".datetime.minToday shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }

    "minTohour" in new Context {
      "2017/08/26 11:33:54".datetime.minTohour shouldBe ZonedDateTime.of(2017, 8, 26, 11, 0, 0, 0, tz.ZONE_ID)
    }

    "maxToday" in new Context {
      "2017/08/26 11:33:54".datetime.maxToday shouldBe ZonedDateTime.of(2017, 8, 26, 23, 59, 59, 999999999, tz.ZONE_ID)
    }

    "maxTohour" in new Context {
      "2017/08/26 11:33:54".datetime.maxTohour shouldBe ZonedDateTime.of(2017, 8, 26, 11, 59, 59, 999999999, tz.ZONE_ID)
    }

    "epoch" in new Context {
      "2017/08/26 11:33:54".datetime.epoch shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID).toEpochSecond
    }
  }

  "UnixTimeBs" should {
    "as" in new Context {
      ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID).toEpochSecond.datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
  }

  "LocalDateTimeBs" should {
    "epoch" in new Context {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54)
        .epoch shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, ZoneId.of("Asia/Tokyo")).toEpochSecond
    }
    "format" in new Context {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54)
        .format() shouldBe "2017/8/26 11:33:54"
    }
    "toZonedDateTime" in new Context {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54)
        .toZonedDateTime shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, ZoneId.of("Asia/Tokyo"))
    }
  }
}
