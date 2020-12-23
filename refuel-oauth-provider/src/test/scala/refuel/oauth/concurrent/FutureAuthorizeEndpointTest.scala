package refuel.oauth.concurrent

import akka.http.scaladsl.model.{HttpRequest, Uri}
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.action.OAuth2ActionHandler
import refuel.oauth.authorize.{AuthorizeRequest, CodeChallenge, ResponseType}
import refuel.oauth.authorize.AuthorizeRequest.AuthorizationCodeRequest
import refuel.oauth.client.AuthorizedApp
import refuel.oauth.exception.{AuthorizeChallengeFailed, AuthorizeRejectionException}
import refuel.oauth.grant.GrantHandler

import scala.concurrent.{ExecutionContext, Future}

class FutureAuthorizeEndpointTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with Injector
    with AsyncMockFactory {
  implicit val ex: ExecutionContext = ExecutionContext.Implicits.global

  case class MyApp(clientId: String = "AAA", clientSecret: String = "BBB", callbackUri: Iterable[Uri] = None)
      extends AuthorizedApp

  "unsupportedGrantType" should {
    "Create from AuthorizeRequest - used state and redirection" in {
      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .unsupportedGrantType(
          AuthorizationCodeRequest(
            "test",
            None,
            None,
            Some("state"),
            Uri("aaa")
          )
        )
        .apply(HttpRequest())
        .map { res =>
          res
            .getHeader("Location")
            .get()
            .value() shouldBe "aaa?error=invalid_request&state=state&error_description=Unsupported+grant+type."
          res.status.intValue() shouldBe 302
        }
    }
  }

  "buildRequest" should {
    "No specified client_id" in {
      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map(),
          null
        )
        .map(_ => fail())
        .recover {
          case e: AuthorizeRejectionException =>
            e.getMessage shouldBe "invalid_request: client_id is required but was no found."
        }
    }
    "Failed to get client application" in {
      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map("client_id" -> "aaa"), {
            val m = mock[GrantHandler[Future, String]]
            (m.findApp _).expects(*).returns(Future.failed(new Exception("ex")))
            m
          }
        )
        .map(_ => fail())
        .recover {
          case e: AuthorizeRejectionException =>
            e.getMessage shouldBe "invalid_request: ex"
        }
    }
    "Failed to verify redirection setting" in {
      val app = MyApp(callbackUri = Seq(Uri("foo"), Uri("bar")))

      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map("client_id" -> "aaa"), {
            val m = mock[GrantHandler[Future, String]]
            (m.findApp _).expects(*).returns(Future(app))
            m
          }
        )
        .map(_ => fail())
        .recover {
          case e: AuthorizeRejectionException =>
            e.getMessage shouldBe "invalid_request: Invalid redirection setting."
        }
    }
    "Invalid grant scope" in {
      val app = MyApp(callbackUri = Some(Uri("foo")))

      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map("client_id" -> "aaa", "response_type" -> "code", "scope" -> "aaa bbb"), {
            val m = mock[GrantHandler[Future, String]]
            (m.findApp _).expects(*).returns(Future(app))
            (m.verifyGrantScope _).expects(*).returns(Future.failed(new Exception("ex")))
            m
          }
        )
        .map(_ => fail())
        .recover {
          case e: AuthorizeChallengeFailed =>
            e.getMessage shouldBe "Failure redirection to foo?error=invalid_request&error_description=ex"
            e.cause.getMessage shouldBe "ex"
        }
    }
    "Invalid challenge code" in {
      val app = MyApp(callbackUri = Some(Uri("foo")))

      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map("client_id" -> "aaa", "response_type" -> "code", "scope" -> "aaa bbb", "code_challenge" -> "iii"), {
            val m = mock[GrantHandler[Future, String]]
            (m.findApp _).expects(*).returns(Future(app))
            (m.verifyGrantScope _).expects(*).returns(Future(Nil))
            m
          }
        )
        .map(_ => fail())
        .recover {
          case e: AuthorizeChallengeFailed =>
            e.getMessage shouldBe "Failure redirection to foo?error=invalid_request&error_description=Both+code_challenge+and+code_challenge_method+must+be+specified,+or+none+of+them"
            e.cause.getMessage shouldBe "Both code_challenge and code_challenge_method must be specified, or none of them"
        }
    }

    "Valid authorize" in {

      val app = MyApp(callbackUri = Some(Uri("foo")))

      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map(
            "client_id"             -> "aaa",
            "response_type"         -> "code",
            "scope"                 -> "aaa bbb",
            "code_challenge"        -> "iii",
            "code_challenge_method" -> "S256",
            "state"                 -> "state"
          ), {
            val m = mock[GrantHandler[Future, String]]
            (m.findApp _).expects(*).returns(Future(app))
            (m.verifyGrantScope _).expects(*).returns(Future(Nil))
            m
          }
        )
        .map { x =>
          x shouldBe AuthorizeRequest(
            ResponseType.Code,
            "aaa",
            Nil,
            Some(CodeChallenge.pure("iii", "S256").get),
            Some("state"),
            Uri("foo"),
            Map(
              "state"                 -> "state",
              "scope"                 -> "aaa bbb",
              "client_id"             -> "aaa",
              "code_challenge"        -> "iii",
              "code_challenge_method" -> "S256",
              "response_type"         -> "code"
            )
          )
        }
    }

    "No specified grant scopes." in {

      val app = MyApp(callbackUri = Some(Uri("foo")))

      new FutureAuthorizeEndpoint(new OAuth2ActionHandler())
        .buildRequest(
          Map(
            "client_id"             -> "aaa",
            "response_type"         -> "code",
            "code_challenge"        -> "iii",
            "code_challenge_method" -> "S256",
            "state"                 -> "state"
          ), {
            val m = mock[GrantHandler[Future, String]]
            (m.findApp _).expects(*).returns(Future(app))
            (m.verifyGrantScope _).expects(*).returns(Future(Nil))
            m
          }
        )
        .map { x =>
          x shouldBe AuthorizeRequest(
            ResponseType.Code,
            "aaa",
            Nil,
            Some(CodeChallenge.pure("iii", "S256").get),
            Some("state"),
            Uri("foo"),
            Map(
              "state"                 -> "state",
              "client_id"             -> "aaa",
              "code_challenge"        -> "iii",
              "code_challenge_method" -> "S256",
              "response_type"         -> "code"
            )
          )
        }
    }
  }
}
