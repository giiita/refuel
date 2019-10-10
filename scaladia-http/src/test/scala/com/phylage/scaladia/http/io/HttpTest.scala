package com.phylage.scaladia.http.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.phylage.scaladia.http.io.Http._
import com.phylage.scaladia.http.io.HttpMethod.GET
import com.phylage.scaladia.http.io.HttpTest.TestEntity.Jokes
import com.phylage.scaladia.http.io.setting.HttpSetting
import com.phylage.scaladia.injector.Injector
import org.scalatest._

object HttpTest {

  object TestEntity {

    case class JokeBody(id: Int,
                        joke: String,
                        categories: Seq[String])

    @JsonIgnoreProperties(ignoreUnknown = true)
    case class Jokes(status: String, value: JokeBody)

  }

}

class HttpTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with Injector {

  "Http io test" should {
    case class InnerJokeBody(id: Int,
                             joke: String,
                             categories: Seq[String])

    @JsonIgnoreProperties(ignoreUnknown = true)
    case class InnerJokes(value: InnerJokeBody)

    "inner class can not deserialize" in {
<<<<<<< HEAD
      http[GET]("http://localhost:3000/endpoint")
=======
      println("RUN 1")
      http[GET]("http://localhost:3289/endpoint")
>>>>>>> a776232... CircleCI http testing setup shell failed.
        .as[InnerJokes]
        .map { x =>
          x.value.joke
        }
        .run
        .map(x => fail(x))
        .recover {
          case e => e.getMessage should startWith("Cannot deseialize to com.phylage.scaladia.http.io.HttpTest")
        }
    }
    "deserializing" in {
<<<<<<< HEAD
      http[GET]("http://localhost:3000/endpoint")
=======
      println("RUN 2")
      http[GET]("http://localhost:3289/endpoint")
>>>>>>> a776232... CircleCI http testing setup shell failed.
        .as[Jokes]
        .map { x =>
          x.value.joke
        }
        .run
        .map { result =>
          println(s"Get joke is [ $result ]")
          result.length > 0 shouldBe true
        }
    }
    "undeserializing" in {
<<<<<<< HEAD
      http[GET]("http://localhost:3000/endpoint")
=======
      println("RUN 3")
      http[GET]("http://localhost:3289/endpoint")
>>>>>>> a776232... CircleCI http testing setup shell failed.
        .map { x =>
          s"Got it [ $x ]"
        }
        .run
        .map { result =>
          println(result)
          result.length > 0 shouldBe true
        }
    }
    "asString" in {
<<<<<<< HEAD
      http[GET]("http://localhost:3000/endpoint")
=======
      println("RUN 4")
      http[GET]("http://localhost:3289/endpoint")
>>>>>>> a776232... CircleCI http testing setup shell failed.
        .asString
        .run
        .map { result =>
          println(result)
          result.length > 0 shouldBe true
        }
    }
    "UnknownHostException" in {
      println("RUN 5")

      http[GET]("http://aaaaaaaaaaaaaaaaaaaaaaaaaa/bbbbbbbbbbbbbbbbbbbbbbbbb/cccccccccccccccccccccccccccc")
        .asString
        .run
        .map(_ => fail("Can not succeed"))
        .recover {
          case _: akka.stream.StreamTcpException => succeed
          case e => fail(e)
        }
    }
    "ConnectException" in {

      println("RUN 6")
      http[GET]("http://localhost/bbbbbbbbbbbbbbbbbbbbbbbbb/cccccccccccccccccccccccccccc")
        .asString
        .run
        .map(_ => fail("Can not succeed"))
        .recover {
          case _: akka.stream.StreamTcpException => succeed
          case e => fail(e)
        }
    }
  }
}

