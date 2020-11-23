package refuel.lang

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import refuel.injector.Injector

import scala.util.{Failure, Try}

class ScalaTimeTest extends AnyWordSpec with Matchers with Injector {

  val tz: RuntimeTZ = inject[RuntimeTZ]
  val st: ScalaTime = inject[ScalaTime]

  import st._

  "ZonedDateTimeBs" should {

    "parse yyyy/MM/dd HH:mm:ss" in {
      "2017/08/26 11:33:54".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
    "parse yyyyMMddHHmmss" in {
      "20170826113354".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
    "parse yyyy/MM/dd" in {
      "2017/08/26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyyMMdd" in {
      "20170826".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy/M/d" in {
      "2017/8/26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy/M/d H" in {
      "2017/8/26 9".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 9, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-MM-dd HH:mm:ss" in {
      "2017-08-26 11:33:54".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
    }
    "parse yyyy-MM-dd" in {
      "2017-08-26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-M-d" in {
      "2017-8-26".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-M-d H" in {
      "2017-8-26 9".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 9, 0, 0, 0, tz.ZONE_ID)
    }
    "parse yyyy-M-d HH" in {
      "2017-8-26 09".datetime shouldBe ZonedDateTime.of(2017, 8, 26, 9, 0, 0, 0, tz.ZONE_ID)
    }
    "parse unexpected" in {
      Try {
        "2017826".datetime
      } match {
        case Failure(e: IllegalArgumentException) => e.getMessage shouldBe "Unexpected datetime format. 2017826"
        case _                                    => fail()
      }
    }
  }

  "StringBs" should {
    "as default" in {
      "2017/08/26 11:33:54".datetime.format() shouldBe "2017/8/26 11:33:54"
    }
    "as custom" in {
      "2017/08/26 11:33:54".datetime.formatTo("yyMd") shouldBe "17826"
    }

    "minToday" in {
      println("2017/08/26 11:33:54".datetime.minToday)
      "2017/08/26 11:33:54".datetime.minToday shouldBe ZonedDateTime.of(2017, 8, 26, 0, 0, 0, 0, tz.ZONE_ID)
    }

    "minTohour" in {
      "2017/08/26 11:33:54".datetime.minTohour shouldBe ZonedDateTime.of(2017, 8, 26, 11, 0, 0, 0, tz.ZONE_ID)
    }

    "maxToday" in {
      "2017/08/26 11:33:54".datetime.maxToday shouldBe ZonedDateTime.of(2017, 8, 26, 23, 59, 59, 999999999, tz.ZONE_ID)
    }

    "maxTohour" in {
      "2017/08/26 11:33:54".datetime.maxTohour shouldBe ZonedDateTime.of(2017, 8, 26, 11, 59, 59, 999999999, tz.ZONE_ID)
    }

    "epoch" in {
      "2017/08/26 11:33:54".datetime.epoch shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID)
        .toEpochSecond
    }
  }

  "UnixTimeBs" should {
    "as" in {
      ZonedDateTime.of(2017, 8, 26, 11, 33, 54, 0, tz.ZONE_ID).toEpochSecond.datetime shouldBe ZonedDateTime.of(
        2017,
        8,
        26,
        11,
        33,
        54,
        0,
        tz.ZONE_ID
      )
    }
  }

  "LocalDateTimeBs" should {
    "epoch" in {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54).epoch shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, ZoneId.of("Asia/Tokyo"))
        .toEpochSecond
    }
    "format" in {
      LocalDateTime
        .of(2017, 8, 26, 11, 33, 54)
        .format() shouldBe "2017/8/26 11:33:54"
    }
    "toZonedDateTime" in {
      LocalDateTime.of(2017, 8, 26, 11, 33, 54).toZonedDateTime shouldBe ZonedDateTime
        .of(2017, 8, 26, 11, 33, 54, 0, ZoneId.of("Asia/Tokyo"))
    }
  }
}
