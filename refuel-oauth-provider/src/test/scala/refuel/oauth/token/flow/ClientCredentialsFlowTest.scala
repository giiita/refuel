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
import refuel.oauth.authorize.{AuthProfile, AuthorizeState, CodeChallenge}
import refuel.oauth.client.AuthorizedApp
import refuel.oauth.grant.GrantHandler
import refuel.oauth.token.GrantRequest
import refuel.oauth.token.GrantRequest.{ClientCredentialsGrantRequest, Raw}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class ClientCredentialsFlowTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {

  case class MyAuthProfile(ownerProfile: String = "owner", clientId: String = "AAA", scopes: Iterable[GrantScope] = Nil)
      extends AuthProfile[String]

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
        new ClientCredentialsFlow(new OAuth2ActionHandler())
          .sync(
            new ClientCredentialsGrantRequest(
              "aaa",
              "bbb",
              Map()
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
        new ClientCredentialsFlow(new OAuth2ActionHandler()) {
          override def appVerification(req: GrantRequest, app: AuthorizedApp): Try[Int] =
            Failure(new Exception("ex"))
        }.sync(
            new ClientCredentialsGrantRequest(
              "aaa",
              "bbb",
              Map()
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
    "Failure matching partial functions" in {
      Get("/test") ~> get {
        new ClientCredentialsFlow(new OAuth2ActionHandler()) {
          override def appVerification(req: GrantRequest, app: AuthorizedApp): Try[Int] = Success(1)
        }.sync(
            new ClientCredentialsGrantRequest(
              "aaa",
              "bbb",
              Map()
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              (m.verifyCredentials _)
                .expects()
                .returns(
                  {
                    case e: Raw => Future(MyAuthProfile())
                  }
                )
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Matching partial functions, but verification failed" in {
      Get("/test") ~> get {
        new ClientCredentialsFlow(new OAuth2ActionHandler()) {
          override def appVerification(req: GrantRequest, app: AuthorizedApp): Try[Int] = Success(1)
        }.sync(
            new ClientCredentialsGrantRequest(
              "aaa",
              "bbb",
              Map()
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              (m.verifyCredentials _)
                .expects()
                .returns(
                  {
                    case e => Future.failed(new Exception("ex"))
                  }
                )
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
        new ClientCredentialsFlow(new OAuth2ActionHandler()) {
          override def appVerification(req: GrantRequest, app: AuthorizedApp): Try[Int] =
            Success(1)
        }.sync(
            new ClientCredentialsGrantRequest(
              "aaa",
              "bbb",
              Map()
            ), {
              val m = mock[GrantHandler[Future, String]]
              (m.findApp _)
                .expects(*)
                .returns(
                  Future(MyApp())
                )
              (m.verifyCredentials _)
                .expects()
                .returns(
                  {
                    case e => Future(MyAuthProfile())
                  }
                )
              m
            }
          )
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 200
      }
    }
  }
}
