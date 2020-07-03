package refuel.http.io

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import org.scalatest._
import refuel.http.io.Http._
import refuel.http.io.HttpMethod.GET
import refuel.http.io.HttpTest.TestEntity.{InnerJokeBody, Jokes}
import refuel.injector.Injector
import refuel.json.CodecDef

object HttpTest extends Injector {

  object TestEntity {

    case class JokeBody(id: Int, joke: String, categories: Seq[String])

    case class Jokes(status: String, value: JokeBody)

    case class InnerJokeBody(id: Int, joke: String, categories: Seq[String])

    case class InnerJokes(value: InnerJokeBody)

  }

}

class HttpTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with Injector with CodecDef {

  implicit val as: ActorSystem = ActorSystem().index()

  "Http io test" should {

    "inner class can not deserialize" in {
      http[GET]("http://localhost:3289/endpoint")
        .as[InnerJokeBody](CaseClassCodec.from)
        .map(_.joke)
        .run
        .map(x => fail(x))
        .recover {
          case e => e.getMessage should startWith("Internal structure analysis raised an exception.")
        }
    }
    "deserializing" in {
      http[GET]("http://localhost:3289/endpoint")
        .as[Jokes](CaseClassCodec.from)
        .map { x => x.value.joke }
        .run
        .map { result =>
          println(s"Get joke is [ $result ]")
          result.length > 0 shouldBe true
        }
    }
    "undeserializing" in {
      http[GET]("http://localhost:3289/endpoint")
        .map { x => s"Got it [ $x ]" }
        .run
        .map { result =>
          println(result)
          result.length > 0 shouldBe true
        }
    }
    "asString" in {
      http[GET]("http://localhost:3289/endpoint").asString.run
        .map { result =>
          println(result)
          result.length > 0 shouldBe true
        }
    }
    "UnknownHostException" in {
      println("RUN 5")

      http[GET]("http://aaaaaaaaaaaaaaaaaaaaaaaaaa/bbbbbbbbbbbbbbbbbbbbbbbbb/cccccccccccccccccccccccccccc").asString.run
        .map(_ => fail("Can not succeed"))
        .recover {
          case HttpProcessingFailed(_: akka.stream.StreamTcpException) => succeed
          case e                                                       => fail(e)
        }
    }
    "ConnectException" in {

      println("RUN 6")
      http[GET]("http://localhost/bbbbbbbbbbbbbbbbbbbbbbbbb/cccccccccccccccccccccccccccc").asString.run
        .map(_ => fail("Can not succeed"))
        .recover {
          case HttpProcessingFailed(_: akka.stream.StreamTcpException) => succeed
          case e                                                       => fail(e)
        }
    }

    "Pickup client error" in {
      http[GET]("http://localhost:3289/notfound").pickup.run
        .map {
          case HttpResponse(StatusCodes.NotFound, _, _, _) => succeed
          case _                                           => fail()
        }
        .recover {
          case e => fail(e)
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
          case _ => http[GET]("http://localhost:3289/endpoint").asString
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
          case _ => http[GET]("http://localhost:3289/endpoint").asString.run
        }
        .run
        .map(_ => succeed)
        .recover {
          case e => fail()
        }
    }
  }
}
