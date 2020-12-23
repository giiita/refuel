package refuel.oauth.concurrent

import java.util.Base64

import akka.http.scaladsl.model.FormData
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials, OAuth2BearerToken}
import akka.http.scaladsl.server.{Directive1, Directives}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.action.{HttpAction, OAuth2ActionHandler, UnauthorizedAction}
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.grant.{DefaultGrantFlowDispatcher, GrantFlowDispatcher, GrantHandler}
import refuel.oauth.token.GrantRequest

import scala.concurrent.Future
import scala.util.Try

class FutureTokenEndpointTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {
  "grant" should {
    "Unspecified authorization header" in {
      Get("/test") ~> get {
        new FutureTokenEndpoint({
          val m = mock[DefaultGrantFlowDispatcher]
//          (m.dispatch[String] _).expects(*, *).returns(Left(UnauthorizedAction(new Exception("ex"))))
          m
        }, new OAuth2ActionHandler()).grant[String](null).apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Invalid type authorization" in {
      Get("/test").withHeaders(Authorization(OAuth2BearerToken("foo"))) ~> get {
        new FutureTokenEndpoint({
          val m = mock[DefaultGrantFlowDispatcher]
          m
        }, new OAuth2ActionHandler()).grant[String](null).apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Invalid hashed authorization" in {
      Get("/test").withHeaders(Authorization(BasicHttpCredentials("foo"))) ~> get {
        new FutureTokenEndpoint({
          val m = mock[DefaultGrantFlowDispatcher]
          (m.dispatch[String] _).expects(*, *).returns(Left(UnauthorizedAction(new Exception("ex"))))
          m
        }, new OAuth2ActionHandler()).grant[String](null).apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
      }
    }
    "Passed authorization flow to dispatcher" in {
      var resultRequest: GrantRequest = null

      Get("/test")
        .withHeaders(
          Authorization(BasicHttpCredentials(new String(Base64.getEncoder.encode("foo:bar".getBytes()))))
        )
        .withEntity(
          FormData
            .apply(
              "aaa" -> "bbb",
              "ccc" -> "ddd"
            )
            .toEntity
        ) ~> get {
        new FutureTokenEndpoint({
          val m = mock[GrantFlowDispatcher[Future]]
          (m.dispatch[String]: (
              GrantRequest,
              GrantHandler[Future, String]
          ) => Either[HttpAction, Directive1[AuthProfile[String]]]).stubs(*, *).onCall {
            (req: GrantRequest, _: GrantHandler[Future, String]) =>
              resultRequest = req
              Left(UnauthorizedAction(new Exception("ex")))
          }
          m
        }, new OAuth2ActionHandler()).grant[String](null).apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
        resultRequest.clientId shouldBe "foo"
        resultRequest.clientSecret shouldBe "bar"
        resultRequest.parameters shouldBe Map(
          "aaa" -> "bbb",
          "ccc" -> "ddd"
        )
      }
    }
  }
}
