package refuel.oauth.authorize

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.util.Success

class ResponseTypeTest extends AsyncWordSpec with Matchers with Diagrams {
  "Construction" should {
    "code" in {
      ResponseType.apply("code") shouldBe Success(ResponseType.Code)
    }
    "token" in {
      ResponseType.apply("token") shouldBe Success(ResponseType.Token)
    }
    "other" in {
      ResponseType.apply("xxx") shouldBe Success(ResponseType.Extended("xxx"))
    }
  }
}
