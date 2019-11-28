package refuel.lang.period

import refuel.injector.Injector
import org.scalatest.{Matchers, WordSpec}

import refuel.lang.ScalaTime._

class FromToTest extends WordSpec with Matchers with Injector {

  trait Context {

    case class MyPeriod(from: EpochDateTime, to: EpochDateTime) extends FromTo

  }

  "overlap" should {
    "before before" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(0, 99)) shouldBe false
    }
    "before eq" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(0, 100)) shouldBe true
    }
    "before contains" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(0, 101)) shouldBe true
    }
    "eq contains" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(100, 150)) shouldBe true
    }
    "contains contains" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(110, 150)) shouldBe true
    }
    "contains eq" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(110, 200)) shouldBe true
    }
    "contains after" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(110, 201)) shouldBe true
    }
    "eq after" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(200, 201)) shouldBe true
    }
    "after after" in new Context {
      MyPeriod(100, 200).overlap(MyPeriod(201, 300)) shouldBe false
    }
  }

  "contains" should {
    "before" in new Context {
      MyPeriod(100, 200).contains(99) shouldBe false
    }
    "before eq" in new Context {
      MyPeriod(100, 200).contains(100) shouldBe true
    }
    "contains" in new Context {
      MyPeriod(100, 200).contains(150) shouldBe true
    }
    "after eq" in new Context {
      MyPeriod(100, 200).contains(200) shouldBe true
    }
    "after" in new Context {
      MyPeriod(100, 200).contains(201) shouldBe false
    }
  }

  "slice" should {
    "2 hourly slice" in new Context {
      MyPeriod("2019-06-10 13:26:54".datetime.epoch, "2019-06-12 08:12:33".datetime.epoch)
        .slice(sliceAt = 2)(MyPeriod).map(x => x.from.datetime.format() -> x.to.datetime.format()) shouldBe Seq(
        "2019/6/10 13:26:54" -> "2019/6/10 14:59:59",
        "2019/6/10 15:0:0" -> "2019/6/10 16:59:59",
        "2019/6/10 17:0:0" -> "2019/6/10 18:59:59",
        "2019/6/10 19:0:0" -> "2019/6/10 20:59:59",
        "2019/6/10 21:0:0" -> "2019/6/10 22:59:59",
        "2019/6/10 23:0:0" -> "2019/6/11 0:59:59",
        "2019/6/11 1:0:0" -> "2019/6/11 2:59:59",
        "2019/6/11 3:0:0" -> "2019/6/11 4:59:59",
        "2019/6/11 5:0:0" -> "2019/6/11 6:59:59",
        "2019/6/11 7:0:0" -> "2019/6/11 8:59:59",
        "2019/6/11 9:0:0" -> "2019/6/11 10:59:59",
        "2019/6/11 11:0:0" -> "2019/6/11 12:59:59",
        "2019/6/11 13:0:0" -> "2019/6/11 14:59:59",
        "2019/6/11 15:0:0" -> "2019/6/11 16:59:59",
        "2019/6/11 17:0:0" -> "2019/6/11 18:59:59",
        "2019/6/11 19:0:0" -> "2019/6/11 20:59:59",
        "2019/6/11 21:0:0" -> "2019/6/11 22:59:59",
        "2019/6/11 23:0:0" -> "2019/6/12 0:59:59",
        "2019/6/12 1:0:0" -> "2019/6/12 2:59:59",
        "2019/6/12 3:0:0" -> "2019/6/12 4:59:59",
        "2019/6/12 5:0:0" -> "2019/6/12 6:59:59",
        "2019/6/12 7:0:0" -> "2019/6/12 8:12:33"
      )
    }

    "just size slice" in new Context {
      MyPeriod("2019/6/10 13:0:0".datetime.epoch, "2019/6/10 23:0:0".datetime.epoch)
        .slice(sliceAt = 2)(MyPeriod).map(x => x.from.datetime.format() -> x.to.datetime.format()) shouldBe Seq(
        "2019/6/10 13:0:0" -> "2019/6/10 14:59:59",
        "2019/6/10 15:0:0" -> "2019/6/10 16:59:59",
        "2019/6/10 17:0:0" -> "2019/6/10 18:59:59",
        "2019/6/10 19:0:0" -> "2019/6/10 20:59:59",
        "2019/6/10 21:0:0" -> "2019/6/10 23:0:0"
      )
    }

    "unslices" in new Context {
      MyPeriod("2019/6/10 13:0:0".datetime.epoch, "2019/6/10 14:50:50".datetime.epoch)
        .slice(sliceAt = 2)(MyPeriod).map(x => x.from.datetime.format() -> x.to.datetime.format()) shouldBe Seq(
        "2019/6/10 13:0:0" -> "2019/6/10 14:50:50"
      )
    }
  }
}
