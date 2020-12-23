package refuel.oauth.token.flow

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.GrantScope
import refuel.oauth.action.OAuth2ActionHandler
import refuel.oauth.authorize.{AuthorizeState, CodeChallenge}
import refuel.oauth.client.AuthorizedApp
import refuel.oauth.exception.{AuthorizeChallengeException, InvalidGrantException}
import refuel.oauth.grant.GrantHandler
import refuel.oauth.token.GrantRequest.AuthorizeCodeGrantRequest

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class AuthorizationCodeFlowTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {

  case class MyApp(clientId: String = "AAA", clientSecret: String = "BBB", callbackUri: Iterable[Uri] = None)
      extends AuthorizedApp

  case class MyAuthState(
      ownerProfile: String = "owner",
      clientId: String = "AAA",
      scopes: Iterable[GrantScope] = Nil,
      codeChallenge: Option[CodeChallenge] = None,
      redirectUri: Option[Uri] = None
  ) extends AuthorizeState[String]

  "sync" should {
    "Failure find app" in {
      Get("/test") ~> get {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .sync(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "xxx",
              None,
              None
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future.failed(new Exception("ex"))
                )
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Failure app verification" in {
      Get("/test") ~> get {
        new AuthorizationCodeFlow(new OAuth2ActionHandler()) {
          override def grantVerification(req: AuthorizeCodeGrantRequest, app: AuthorizedApp): Try[Int] =
            Failure(new Exception("ex"))
        }.sync(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "xxx",
              None,
              None
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Failure restore authorization" in {
      Get("/test") ~> get {
        new AuthorizationCodeFlow(new OAuth2ActionHandler()) {
          override def grantVerification(req: AuthorizeCodeGrantRequest, app: AuthorizedApp): Try[Int] = Success(1)
        }.sync(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "xxx",
              None,
              None
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              (m.restoreAuthorization _).expects(*).returns(Future.failed(new Exception("ex")))
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Failure authorize state verification" in {
      Get("/test") ~> get {
        new AuthorizationCodeFlow(new OAuth2ActionHandler()) {
          override def grantVerification(req: AuthorizeCodeGrantRequest, app: AuthorizedApp): Try[Int] = Success(1)
          override def grantVerification[U](req: AuthorizeCodeGrantRequest, state: AuthorizeState[U]): Try[Int] =
            Failure(new Exception("ex"))
        }.sync(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "xxx",
              None,
              None
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              (m.restoreAuthorization _).expects(*).returns(Future(MyAuthState()))
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Passed all verification" in {
      Get("/test") ~> get {
        new AuthorizationCodeFlow(new OAuth2ActionHandler()) {
          override def grantVerification(req: AuthorizeCodeGrantRequest, app: AuthorizedApp): Try[Int] = Success(1)
          override def grantVerification[U](req: AuthorizeCodeGrantRequest, state: AuthorizeState[U]): Try[Int] =
            Success(1)
        }.sync(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "xxx",
              None,
              None
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              (m.restoreAuthorization _).expects(*).returns(Future(MyAuthState()))
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 200
      }
    }
  }

  "grantVerification" should {
    "Unmatched client secret" in {
      intercept[AuthorizeChallengeException] {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .grantVerification(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "ddd",
              None,
              None
            ),
            MyApp()
          )
          .get
      }.getMessage shouldBe "Invalid grant request."
    }

    "Failure that has an authorization request challenge, but no code_verifier" in {
      intercept[InvalidGrantException] {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .grantVerification(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "ddd",
              None,
              None
            ),
            MyAuthState(codeChallenge = Some(CodeChallenge.pure("foo", "S256").get))
          )
          .get
      }.getMessage shouldBe "Challenge code verification failed."
    }

    "Failure that has not an authorization request challenge, but code_verifier exists." in {
      intercept[InvalidGrantException] {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .grantVerification(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "ddd",
              None,
              Some("foo")
            ),
            MyAuthState()
          )
          .get
      }.getMessage shouldBe "Challenge code verification failed."
    }

    "Failure verify challenge code." in {
      intercept[InvalidGrantException] {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .grantVerification(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "ddd",
              None,
              Some("foo")
            ),
            MyAuthState(codeChallenge = Some(CodeChallenge.pure("bar", "plain").get))
          )
          .get
      }.getMessage shouldBe "Challenge code verification failed."
    }

    "Successfull verify challenge code." in {
      new AuthorizationCodeFlow(new OAuth2ActionHandler())
        .grantVerification(
          new AuthorizeCodeGrantRequest(
            "aaa",
            "bbb",
            Map(),
            "ddd",
            None,
            Some("foo")
          ),
          MyAuthState(codeChallenge = Some(CodeChallenge.pure("foo", "plain").get))
        )
        .get shouldBe 1
    }

    "Invalid redirect uri history" in {
      intercept[InvalidGrantException] {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .grantVerification(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "ddd",
              None,
              None
            ),
            MyAuthState(redirectUri = Some(Uri("foo")))
          )
          .get
      }.getMessage shouldBe "Invalid redirection."
    }

    "Invalid redirect uri history 2" in {
      intercept[InvalidGrantException] {
        new AuthorizationCodeFlow(new OAuth2ActionHandler())
          .grantVerification(
            new AuthorizeCodeGrantRequest(
              "aaa",
              "bbb",
              Map(),
              "ddd",
              Some(Uri("foo")),
              None
            ),
            MyAuthState(redirectUri = None)
          )
          .get
      }.getMessage shouldBe "Invalid redirection."
    }

    "Successfull redirect uri history" in {
      new AuthorizationCodeFlow(new OAuth2ActionHandler())
        .grantVerification(
          new AuthorizeCodeGrantRequest(
            "aaa",
            "bbb",
            Map(),
            "ddd",
            Some(Uri("foo")),
            None
          ),
          MyAuthState(redirectUri = Some(Uri("foo")))
        )
        .get shouldBe 1
    }
  }
}
