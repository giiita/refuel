package refuel.oauth.authorize

import akka.http.scaladsl.model.Uri
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.oauth.GrantScope
import refuel.oauth.authorize.AuthorizeRequest.{AuthorizationCodeRequest, ImplicitGrantRequest}

class AuthorizeRequestTest extends AsyncWordSpec with Matchers with Diagrams with Injector {

  case object TestGrantScope extends GrantScope {
    def id: String = "test"
  }

  "Authorize request construction" should {
    "code" in {
      val uri = Uri("ddd")
      val req = AuthorizeRequest
        .apply(
          ResponseType.Code,
          "aaa",
          Seq(TestGrantScope),
          codeChallenge = CodeChallenge.pure("bbb", "plain").toOption,
          state = Some("ccc"),
          redirectUri = uri
        )
        .asInstanceOf[AuthorizationCodeRequest]
      req.clientId shouldBe "aaa"
      req.scopes.toSeq shouldBe Seq(TestGrantScope)
      req.codeChallenge shouldBe CodeChallenge.pure("bbb", "plain").toOption
      req.state shouldBe Some("ccc")
      req.redirectUri shouldBe uri
    }
    "token" in {
      val uri = Uri("ddd")
      val req = AuthorizeRequest
        .apply(
          ResponseType.Token,
          "aaa",
          Some(TestGrantScope),
          codeChallenge = CodeChallenge.pure("bbb", "plain").toOption,
          state = Some("ccc"),
          redirectUri = Uri("ddd")
        )
        .asInstanceOf[ImplicitGrantRequest]
      req.clientId shouldBe "aaa"
      req.scopes.toSeq shouldBe Seq(TestGrantScope)
      req.state shouldBe Some("ccc")
      req.redirectUri shouldBe uri
    }
  }
}
