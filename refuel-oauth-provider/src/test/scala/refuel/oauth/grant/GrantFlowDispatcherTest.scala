package refuel.oauth.grant

import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.{Directive1, Directives}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.GrantScope
import refuel.oauth.action.{OAuth2ActionHandler, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.token.GrantRequest
import refuel.oauth.token.GrantRequest._
import refuel.oauth.token.flow.{AuthorizationCodeFlow, ClientCredentialsFlow, PasswordCredentialsFlow, RefreshTokenFlow}

import scala.concurrent.Future

class GrantFlowDispatcherTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {
  "dispatch" should {
    "No specified grant type" in {
      val e =
        new DefaultGrantFlowDispatcher(null, null, null, null)
          .dispatch[String](Raw(BasicHttpCredentials("aaa", "bbb"), Map()), null)
      assert(e.isLeft)
      e.left.get match {
        case a @ UnauthorizedAction(cause) =>
          a.status.intValue() shouldBe 401
          cause.getMessage shouldBe "grant_type is required but was no found."
        case _ => fail()
      }
    }
    "Unfollowed grant type" in {
      val e =
        new DefaultGrantFlowDispatcher(null, null, null, null)
          .dispatch[String](Raw(BasicHttpCredentials("aaa", "bbb"), Map("grant_type" -> "unfollowed")), null)
      assert(e.isLeft)
      e.left.get match {
        case a @ UnauthorizedAction(cause) =>
          a.status.intValue() shouldBe 401
          cause.getMessage shouldBe "Unsupported grant type."
        case _ => fail()
      }
    }
    "Dispatch flow successfully" in {
      var resultRequest: GrantRequest     = null
      var flowResult: AuthProfile[String] = null

      val profile = new AuthProfile[String] {
        override val ownerProfile: String = "xxx"
        override val clientId: String     = "yyy"
        override val scopes: Iterable[GrantScope] = Seq(new GrantScope {
          override def id: String = "zzz"
        })
      }

      val clientCredentialFlow = {
        new ClientCredentialsFlow(new OAuth2ActionHandler()) {
          override def sync[U](
              v1: ClientCredentialsGrantRequest,
              grantHandler: GrantHandler[Future, U]
          ): Directive1[AuthProfile[U]] = {
            resultRequest = v1
            provide(profile.asInstanceOf[AuthProfile[U]])
          }
        }
      }

      val e =
        new DefaultGrantFlowDispatcher(null, null, null, clientCredentialFlow)
          .dispatch[String](Raw(BasicHttpCredentials("aaa", "bbb"), Map("grant_type" -> "client_credentials")), null)
      assert(e.isRight)

      Get("/test") ~> get {
        e.right.get.apply { x =>
          flowResult = x
          complete("x")
        }
      } ~> check {
        resultRequest.clientId shouldBe "aaa"
        resultRequest.clientSecret shouldBe "bbb"
        resultRequest.parameters shouldBe Map("grant_type" -> "client_credentials")

        flowResult shouldBe profile
      }
    }

    "Verify applyFlow target flow" in {
      var mode                       = 0
      var grantRequest: GrantRequest = null

      val profile = new AuthProfile[String] {
        override val ownerProfile: String = "xxx"
        override val clientId: String     = "yyy"
        override val scopes: Iterable[GrantScope] = Seq(new GrantScope {
          override def id: String = "zzz"
        })
      }

      val dis =
        new DefaultGrantFlowDispatcher(
          new AuthorizationCodeFlow(new OAuth2ActionHandler()) {
            override def sync[U](
                v1: AuthorizeCodeGrantRequest,
                grantHandler: GrantHandler[Future, U]
            ): Directive1[AuthProfile[U]] = {
              mode = 1
              grantRequest = v1
              provide(profile.asInstanceOf[AuthProfile[U]])
            }
          },
          new RefreshTokenFlow(new OAuth2ActionHandler()) {
            override def sync[U](
                v1: RefreshTokenGrantRequest,
                grantHandler: GrantHandler[Future, U]
            ): Directive1[AuthProfile[U]] = {
              mode = 2
              grantRequest = v1
              provide(profile.asInstanceOf[AuthProfile[U]])
            }
          },
          new PasswordCredentialsFlow(new OAuth2ActionHandler()) {
            override def sync[U](
                v1: PasswordGrantRequest,
                grantHandler: GrantHandler[Future, U]
            ): Directive1[AuthProfile[U]] = {
              mode = 3
              grantRequest = v1
              provide(profile.asInstanceOf[AuthProfile[U]])
            }
          },
          new ClientCredentialsFlow(new OAuth2ActionHandler()) {
            override def sync[U](
                v1: ClientCredentialsGrantRequest,
                grantHandler: GrantHandler[Future, U]
            ): Directive1[AuthProfile[U]] = {
              mode = 4
              grantRequest = v1
              provide(profile.asInstanceOf[AuthProfile[U]])
            }
          }
        )

      Get("/test") ~> get {
        dis
          .dispatch[String](
            Raw(BasicHttpCredentials("aaa", "bbb"), Map("grant_type" -> "authorization_code", "code" -> "aaa")),
            null
          )
          .right
          .get
          .apply { _ => complete("x") }
      } ~> check {
        mode shouldBe 1
        assert(grantRequest.isInstanceOf[AuthorizeCodeGrantRequest])
      }

      Get("/test") ~> get {
        dis
          .dispatch[String](
            Raw(BasicHttpCredentials("aaa", "bbb"), Map("grant_type" -> "refresh_token", "refresh_token" -> "aaa")),
            null
          )
          .right
          .get
          .apply { _ => complete("x") }
      } ~> check {
        mode shouldBe 2
        assert(grantRequest.isInstanceOf[RefreshTokenGrantRequest])
      }

      Get("/test") ~> get {
        dis
          .dispatch[String](
            Raw(
              BasicHttpCredentials("aaa", "bbb"),
              Map("grant_type" -> "password", "username" -> "", "password" -> "")
            ),
            null
          )
          .right
          .get
          .apply { _ => complete("x") }
      } ~> check {
        mode shouldBe 3
        assert(grantRequest.isInstanceOf[PasswordGrantRequest])
      }

      Get("/test") ~> get {
        dis
          .dispatch[String](Raw(BasicHttpCredentials("aaa", "bbb"), Map("grant_type" -> "client_credentials")), null)
          .right
          .get
          .apply { _ => complete("x") }
      } ~> check {
        mode shouldBe 4
        assert(grantRequest.isInstanceOf[ClientCredentialsGrantRequest])
      }
    }
  }
}
