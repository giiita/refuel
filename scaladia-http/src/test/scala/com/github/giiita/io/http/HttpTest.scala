package com.github.giiita.io.http

import com.github.giiita.io.http.Http._
import com.github.giiita.io.http.HttpMethod._
import com.github.giiita.io.http.HttpTest.Test
import org.scalatest.{AsyncWordSpec, Matchers}

object HttpTest {
  case class Test(name: String,
                  gender: String,
                  probability: Int,
                  count: Int)
}

class HttpTest extends AsyncWordSpec with Matchers {
  "http test" should {
    "status 302" in {
      http[POST]("http://hooks.slack.com/test")
        .run
        .map(_ => fail())
        .recover {
          case _ => succeed
        }
    }

    "status 200 as string" in {
      http[GET]("https://api.genderize.io/?name=jon")
        .run
        .map { x =>
          x shouldBe """{"name":"jon","gender":"male","probability":1,"count":1790}"""
        }
    }

    "status 200 as Test" in {
      http[GET]("https://api.genderize.io/?name=jon")
        .deserializing[Test]
        .run
        .map { x =>
          x.name shouldBe "jon"
          x.gender shouldBe "male"
          x.probability shouldBe 1
          x.count shouldBe 1790
        }
    }
  }
}
