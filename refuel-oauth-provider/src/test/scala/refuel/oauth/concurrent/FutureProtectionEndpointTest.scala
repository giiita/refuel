package refuel.oauth.concurrent

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.GrantScope
import refuel.oauth.action.OAuth2ActionHandler
import refuel.oauth.authorize.AuthProfile
import refuel.oauth.grant.GrantHandler

import scala.concurrent.Future

class FutureProtectionEndpointTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {

  "verify" should {
    "Failure verifyAccessToken" in {
      Get("/test") ~> get {
        new FutureProtectionEndpoint(new OAuth2ActionHandler())
          .verify[String]("", {
            val m = mock[GrantHandler[Future, String]]
            (m.verifyAccessToken _).expects(*).returns(Future.failed(new Exception("")))
            m
          })
          .apply(_ => complete("1"))
      } ~> check {
        status.intValue() shouldBe 401
        header("Location") shouldBe None
        responseAs[String] shouldBe "Authentication is possible but has failed or not yet been provided."
      }
    }
    "Success verifyAccessToken" in {
      case class MyScope(id: String) extends GrantScope
      Get("/test") ~> get {
        new FutureProtectionEndpoint(new OAuth2ActionHandler())
          .verify[String](
            "", {
              val m = mock[GrantHandler[Future, String]]
              (m.verifyAccessToken _)
                .expects(*)
                .returns(Future(new AuthProfile[String] {
                  override val ownerProfile: String         = "aaa"
                  override val clientId: String             = "bbb"
                  override val scopes: Iterable[GrantScope] = Seq(MyScope("foo"), MyScope("bar"))
                }))
              m
            }
          )
          .apply(x => complete(s"${x.ownerProfile}, ${x.clientId}, ${x.scopes.serialize}"))
      } ~> check {
        status.intValue() shouldBe 200
        header("Location") shouldBe None
        responseAs[String] shouldBe "aaa, bbb, foo bar"
      }
    }
  }
}
