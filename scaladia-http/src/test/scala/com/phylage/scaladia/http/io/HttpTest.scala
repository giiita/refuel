package com.phylage.scaladia.http.io

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.phylage.scaladia.http.io.Http._
import com.phylage.scaladia.http.io.HttpMethod.GET
import com.phylage.scaladia.http.io.HttpTest.TestEntity.Jokes
import com.phylage.scaladia.injector.Injector
import org.scalatest._

object HttpTest {
  object TestEntity {

    case class JokeBody(id: Int,
                        joke: String,
                        categories: Seq[String])

    @JsonIgnoreProperties(ignoreUnknown = true)
    case class Jokes(`type`: String, value: JokeBody)

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
      http[GET]("http://api.icndb.com/jokes/random")
        .as[InnerJokes]
        .map { x =>
          x.value.joke
        }
        .run
        .map(x => fail(x))
        .recover {
          case e => e.getMessage startsWith "Cannot construct instance of `com.phylage.scaladia.http.io.HttpTest$" shouldBe true
        }
    }
    "deserializing" in {
      http[GET]("http://api.icndb.com/jokes/random")
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
      http[GET]("http://api.icndb.com/jokes/random")
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
      http[GET]("http://api.icndb.com/jokes/random")
        .asString
        .run
        .map { result =>
          println(result)
          result.length > 0 shouldBe true
        }
    }
    "UnknownHostException" in {
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

