package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import org.scalatest
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.http.io.Http._
import refuel.http.io.HttpMethod.GET
import refuel.injector.Injector
import refuel.json.codecs.Read
import refuel.json.{Codec, CodecDef}

object HttpTest extends Injector {

  object TestEntity {

    case class JokeBody(id: Int, joke: String, categories: Seq[String])

    case class Jokes(status: String, value: JokeBody)

    case class ErrorJokes(status: String, value: JokeBody) extends Throwable

    case class InnerJokeBody(id: Int, joke: String, categories: Seq[String])

    case class InnerJokes(value: InnerJokeBody)

    case class Errors(status: String, error: String) extends Throwable
  }

}

class HttpTest extends AsyncWordSpec with Matchers with Diagrams with Injector with CodecDef {

  import HttpTest.TestEntity._

  implicit val as: ActorSystem = ActorSystem().index()

  "Http io test" should {

    "inner class can not deserialize" in {
      implicit val codec: Codec[InnerJokeBody] = CaseClassCodec.from[InnerJokeBody]
      http[GET]("http://localhost:3289/success")
        .as[InnerJokeBody]
        .run
        .map(_ => fail())
        .recover {
          case e =>
            e.getCause.getMessage shouldBe "Internal structure analysis by class refuel.http.io.HttpTest$$anon$1 raised an exception."
        }
    }
    "deserializing" in {
      implicit val codec: Codec[Jokes] = CaseClassCodec.from[Jokes]
      http[GET]("http://localhost:3289/success")
        .as[Jokes]
        .map { x => x.value.joke }
        .run
        .map { result => result.length > 0 shouldBe true }
    }
    "undeserializing" in {
      http[GET]("http://localhost:3289/success")
        .map { x => s"Got it [ $x ]" }
        .run
        .map { result => result.length > 0 shouldBe true }
    }
    "UnknownHostException" in {
      http[GET]("http://aaaaaaaaaaaaaaaaaaaaaaaaaa/bbbbbbbbbbbbbbbbbbbbbbbbb/cccccccccccccccccccccccccccc").asString.run
        .map(_ => fail("Can not succeed"))
        .recover {
          case _: akka.stream.StreamTcpException => succeed
          case e                                 => fail(e)
        }
    }
    "ConnectException" in {
      http[GET]("http://localhost/bbbbbbbbbbbbbbbbbbbbbbbbb/cccccccccccccccccccccccccccc").asString.run
        .map(_ => fail("Can not succeed"))
        .recover {
          case _: akka.stream.StreamTcpException => succeed
          case e                                 => fail(e)
        }
    }

    "recover" in {
      http[GET]("http://localhost:3289/notfound")
        .recover {
          case _ => "recovered"
        }
        .run
        .map(_ shouldBe "recovered")
        .recover {
          case _ => fail()
        }
    }

    "recover with" in {
      http[GET]("http://localhost:3289/notfound").asString
        .recoverWith[String] {
          case _ => http[GET]("http://localhost:3289/success").asString
        }
        .run
        .map(_ => succeed)
        .recover {
          case e => fail()
        }
    }

    "recover Future" in {
      http[GET]("http://localhost:3289/notfound").asString
        .recoverF[String] {
          case _ => http[GET]("http://localhost:3289/success").asString.run
        }
        .run
        .map(_ => succeed)
        .recover {
          case _ => fail()
        }
    }

    "eitherMap if success" in {
      implicit val _c1: Codec[Jokes]  = CaseClassCodec.from[Jokes]
      implicit val _c2: Codec[Errors] = CaseClassCodec.from[Errors]
      http[GET]("http://localhost:3289/200/success")
        .eitherMap[Errors, Jokes]
        .map {
          case Right(x) =>
            x.status shouldBe "success"
            x.value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          case Left(_) => fail()
        }
        .run
    }

    "eitherMap if failed" in {
      implicit val _c1: Codec[Jokes]  = CaseClassCodec.from[Jokes]
      implicit val _c2: Codec[Errors] = CaseClassCodec.from[Errors]
      http[GET]("http://localhost:3289/200/failed")
        .eitherMap[Errors, Jokes]
        .map {
          case Right(_) =>
            fail()
          case Left(x) =>
            x.status shouldBe "failed"
            x.error shouldBe "foo"
        }
        .run
    }

    "eitherTransform if success" in {
      implicit val _c1: Codec[Jokes]  = CaseClassCodec.from[Jokes]
      implicit val _c2: Codec[Errors] = CaseClassCodec.from[Errors]
      http[GET]("http://localhost:3289/200/success")
        .eitherTransform[Errors, Jokes, Errors]
        .map {
          case Right(x) =>
            x.status shouldBe "success"
            x.value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          case Left(_) => fail()
        }
        .run
    }

    "eitherTransform if 200 failed" in {
      implicit val _c1: Codec[Jokes]  = CaseClassCodec.from[Jokes]
      implicit val _c2: Codec[Errors] = CaseClassCodec.from[Errors]
      http[GET]("http://localhost:3289/200/failed")
        .eitherTransform[Errors, Jokes, Errors]
        .map {
          case Right(_) =>
            fail()
          case Left(x) =>
            x.status shouldBe "failed"
            x.error shouldBe "foo"
        }
        .run
    }

    "eitherTransform if 200 failed and processing failed" in {
      implicit val _c1: Codec[Jokes]  = CaseClassCodec.from[Jokes]
      implicit val _c2: Codec[Errors] = CaseClassCodec.from[Errors]
      http[GET]("http://localhost:3289/200/failed")
        .eitherTransform[Int, Jokes, Errors]
        .map { _ => fail() }
        .recover {
          case _ => succeed
        }
        .run
    }
  }

  "asString" should {
    "Success" in {
      http[GET]("http://localhost:3289/success").asString.run
        .map {
          _ shouldBe s"""{
                        |  "status": "success",
                        |  "value": {
                        |    "id": 90,
                        |    "joke": "Chuck Norris always knows the EXACT location of Carmen SanDiego.",
                        |    "categories": []
                        |  }
                        |}""".stripMargin
        }
    }
    "Failure" in {
      http[GET]("http://localhost:3289/failed").asString.run
        .map(_ => fail())
        .recover {
          case HttpErrorRaw(res, _) =>
            res.status.intValue() shouldBe 500
        }
    }
  }

  "transform" should {

    implicit val _c1: Codec[Jokes]     = CaseClassCodec.from[Jokes]
    implicit val _c2: Read[ErrorJokes] = CaseClassCodec.from[ErrorJokes]
    implicit val _c3: Read[Errors]     = CaseClassCodec.from[Errors]
    implicit val _c4: Read[Exception]  = Deserialize(x => new Exception(x.named("error").des[String]))

    "Success" in {
      http[GET]("http://localhost:3289/success")
        .transform[Jokes, ErrorJokes]
        .run
        .map { x =>
          x.status shouldBe "success"
          x.value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
        }
    }
    "Failed by status code: 500" in {
      http[GET]("http://localhost:3289/failed")
        .transform[Jokes, ErrorJokes]
        .map[scalatest.Assertion](_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case ErrorJokes(status, value) => {
            status shouldBe "failed"
            value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          }
        }
    }
    "Failed by deserialization" in {
      http[GET]("http://localhost:3289/success")
        .transform[Int, ErrorJokes]
        .map[scalatest.Assertion](_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case ErrorJokes(status, value) => {
            status shouldBe "success"
            value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          }
        }
    }
    "Success with either deserialization to right" in {
      http[GET]("http://localhost:3289/success")
        .transform[Either[Int, Jokes], ErrorJokes]
        .run
        .map { x =>
          x.fold(
            _ => fail(),
            x => {
              x.status shouldBe "success"
              x.value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
            }
          )

        }
    }
    "Failed by either deserialization to right" in {
      http[GET]("http://localhost:3289/success")
        .transform[Either[ErrorJokes, Int], Exception]
        .run
        .map { x =>
          x.fold(
            {
              case ErrorJokes(status, value) => {
                status shouldBe "success"
                value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
              }
            },
            _ => fail()
          )
        }
    }
    "Failed by either deserialization to right and left" in {
      http[GET]("http://localhost:3289/success")
        .transform[Either[Int, Int], ErrorJokes]
        .map(_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case ErrorJokes(status, value) => {
            status shouldBe "success"
            value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          }
        }
    }
    "Failed by deserialization for all" in {
      http[GET]("http://localhost:3289/success")
        .transform[Either[Int, Int], Exception]
        .map(_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case HttpErrorRaw(res, _) =>
            res.status.intValue() shouldBe 200
        }
    }
  }

  "eitherTransform" should {

    implicit val codec: Codec[Jokes]       = CaseClassCodec.from[Jokes]
    implicit val _codec: Codec[ErrorJokes] = CaseClassCodec.from[ErrorJokes]
    implicit val _r: Read[Errors]          = CaseClassCodec.from[Errors]
    implicit val _e: Read[Exception]       = Deserialize(x => new Exception(x.named("error").des[String]))

    "Success with either deserialization to right" in {
      http[GET]("http://localhost:3289/success")
        .eitherTransform[Int, Jokes, ErrorJokes]
        .run
        .map { x =>
          x.fold(
            _ => fail(),
            x => {
              x.status shouldBe "success"
              x.value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
            }
          )

        }
    }
    "Failed by either deserialization to right" in {
      http[GET]("http://localhost:3289/success")
        .eitherTransform[ErrorJokes, Int, Exception]
        .run
        .map { x =>
          x.fold(
            {
              case ErrorJokes(status, value) => {
                status shouldBe "success"
                value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
              }
            },
            _ => fail()
          )
        }
    }
    "Failed by either deserialization to right and left" in {
      http[GET]("http://localhost:3289/success")
        .eitherTransform[Int, Int, ErrorJokes]
        .map(_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case ErrorJokes(status, value) => {
            status shouldBe "success"
            value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          }
        }
    }
    "Failed by deserialization for all" in {
      http[GET]("http://localhost:3289/success")
        .eitherTransform[Int, Int, Exception]
        .map(_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case HttpErrorRaw(res, _) =>
            res.status.intValue() shouldBe 200
        }
    }
  }
}
