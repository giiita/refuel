package refuel.oauth.exception

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector

class AuthorizeChallengeFailedTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with ScalatestRouteTest
    with Injector
    with Directives
    with AsyncMockFactory {
  "construction" should {
    "No specified uri" in {
      val e = AuthorizeChallengeFailed.build(
        "foo",
        Some("state"),
        new Exception("ex"),
        None
      )
      assert(e.isInstanceOf[AuthorizeRejectionException])
      e.getMessage shouldBe "foo: ex"
    }
    "Specified uri" in {
      val e = AuthorizeChallengeFailed.build(
        "foo",
        Some("state"),
        new Exception("ex"),
        Some(Uri("foo"))
      )
      assert(e.isInstanceOf[AuthorizeChallengeFailed])
      e.getMessage shouldBe "Failure redirection to foo?error=foo&error_description=ex"
    }
    "toQueryMap inspection" in {
      val e = AuthorizeChallengeFailed(
        Uri("foo"),
        Some("state"),
        "foo",
        Some("ex"),
        new Exception("ex")
      )
      e.toQueryMap shouldBe Map(
        "error"             -> "foo",
        "state"             -> "state",
        "error_description" -> "ex"
      )
    }
  }
}
