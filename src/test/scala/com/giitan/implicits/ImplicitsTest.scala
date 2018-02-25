package com.giitan.implicits

import org.scalatest.{Matchers, WordSpec}

class ImplicitsTest extends WordSpec with Matchers {
  case class Test1(opt1Some: Option[Test2] = Some(Test2()),
                    opt1None: Option[Test2] = None)

  case class Test2(opt2Some: Option[Test3] = Some(Test3()),
                    opt2None: Option[Test3] = None)

  case class Test3(opt3Some: Option[String] = Some("success"),
                    opt3None: Option[String] = None)

  "implicits" should {
    ">>" in {
      Test1().opt1Some >> (_.opt2Some) >> (_.opt3Some) >>> new Exception("") shouldBe "success"
    }
    ">>>" in {
      try {
        Test1().opt1Some >> (_.opt2None) >> (_.opt3Some) >>> new Exception("success")
        new Exception("Test Failed")
      } catch {
        case e: Exception => e.getMessage shouldBe "success"
      }
    }
    "<<" in {
      case class TestConst(v: String)

      Test1().opt1Some >> (_.opt2Some) >> (_.opt3Some) << TestConst >>> new Exception("failed") shouldBe TestConst("success")
    }
    "||=>" in {
      Test1().opt1None >> (_.opt2None) >> (_.opt3Some) ||=> "another success" shouldBe "another success"
    }
  }
}
