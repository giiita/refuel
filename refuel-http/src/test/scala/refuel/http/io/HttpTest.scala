package refuel.http.io

import akka.actor.ActorSystem
import org.scalatest
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.http.io.Http._
import refuel.http.io.HttpMethod.GET
import refuel.http.io.HttpTest.TestEntity.{Errors, InnerJokeBody, JokeBody, Jokes}
import refuel.injector.Injector
import refuel.json.{Codec, CodecDef}

object HttpTest extends Injector {

  object TestEntity {

    case class JokeBody(id: Int, joke: String, categories: Seq[String])

    case class Jokes(status: String, value: JokeBody)

    case class InnerJokeBody(id: Int, joke: String, categories: Seq[String])

    case class InnerJokes(value: InnerJokeBody)

    case class Errors(status: String, error: String)
  }

}

class HttpTest extends AsyncWordSpec with Matchers with Diagrams with Injector with CodecDef {

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
            e.getCause.getMessage should startWith("Internal structure analysis raised an exception.")
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
    "Failure deserialize" in {
      implicit val codec: Codec[Jokes] = CaseClassCodec.from[Jokes]
      http[GET]("http://localhost:3289/failed")
        .transform[Jokes, Jokes]
        .map[scalatest.Assertion](_ => fail())
        .run
        .recover[scalatest.Assertion] {
          case HttpResponseError(e: Jokes, _) => {
            e.status shouldBe "failed"
            e.value.id shouldBe 90
          }
        }
    }
    "asString" in {
      http[GET]("http://localhost:3289/success").asString.run
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

    "eitherTransform if 500 failed" in {
      implicit val _c1: Codec[Jokes]  = CaseClassCodec.from[Jokes]
      implicit val _c2: Codec[Errors] = CaseClassCodec.from[Errors]
      http[GET]("http://localhost:3289/failed")
        .eitherTransform[Errors, Jokes, Jokes]
        .run
        .map(_ => fail())
        .recover {
          case HttpResponseError(x: Jokes, _) =>
            x.status shouldBe "failed"
            x.value shouldBe JokeBody(90, "Chuck Norris always knows the EXACT location of Carmen SanDiego.", Nil)
          case _ => fail()
        }
    }
  }
}
