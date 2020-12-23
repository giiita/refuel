package refuel.oauth

import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials, OAuth2BearerToken}
import akka.http.scaladsl.server.{Directive1, Directives}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.action.OAuth2ActionHandler
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.endpoint.ProtectionEndpoint
import refuel.oauth.grant.GrantHandler

import scala.concurrent.Future

class OAuth2ProtectionTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {

  class MyProtection(
      override val protection: ProtectionEndpoint[Future],
      override val grantHandler: GrantHandler[Future, String],
      override val actionHandler: OAuth2ActionHandler
  ) extends OAuth2Protection[Future, String]

  case class MyAuthProfile(ownerProfile: String = "owner", clientId: String = "AAA", scopes: Iterable[GrantScope] = Nil)
      extends AuthProfile[String]

  "oauth2" should {
    "No specified authorization header" in {
      Get("/test") ~> new MyProtection(null, null, new OAuth2ActionHandler()).oauth2.apply(_ => complete("")) ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Specified authorization header, but was no bearer type" in {
      Get("/test").withHeaders(Authorization(BasicHttpCredentials("foo"))) ~> new MyProtection(
        null,
        null,
        new OAuth2ActionHandler()
      ).oauth2.apply(_ => complete("")) ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Specified authorization bearer header" in {
      var testedToken                        = ""
      var testedProfile: AuthProfile[String] = null
      Get("/test").withHeaders(Authorization(OAuth2BearerToken("foo"))) ~> new MyProtection(
        {
          new ProtectionEndpoint[Future] {
            override def verify[U](
                accessToken: String,
                grantHandler: GrantHandler[Future, U]
            ): Directive1[AuthProfile[U]] = {
              testedToken = accessToken
              provide[AuthProfile[U]](MyAuthProfile().asInstanceOf[AuthProfile[U]])
            }
          }
        },
        null,
        new OAuth2ActionHandler()
      ).oauth2.apply { prof =>
        testedProfile = prof
        complete("")
      } ~> check {
        status.intValue() shouldBe 200
        testedToken shouldBe "foo"
        testedProfile shouldBe MyAuthProfile()
      }
    }
  }
}
