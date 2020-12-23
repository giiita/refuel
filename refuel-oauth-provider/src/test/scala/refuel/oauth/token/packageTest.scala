package refuel.oauth.token

import akka.http.scaladsl.model.Uri.Query
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector
import refuel.json.{Json, JsonTransform}
import refuel.lang.ScalaTime
import refuel.oauth.GrantScope

class packageTest
    extends AsyncWordSpec
    with Matchers
    with Diagrams
    with Injector
    with AsyncMockFactory
    with JsonTransform {

  case class MyScope(id: String) extends GrantScope

  val st = inject[ScalaTime]._provide
  import st._

  "serialization" should {
    "to json literal" in {
      AccessToken(
        "aaa",
        Some("bbb"),
        Seq(MyScope("ccc"), MyScope("ddd")),
        Some(86400),
        now,
        Map("x" -> "xxx", "y" -> "yyy")
      ).ser match {
        case x =>
          x shouldBe Json.obj(
            "token_type"    -> "Bearer",
            "token"         -> "aaa",
            "refresh_token" -> "bbb",
            "scope"         -> "ccc ddd",
            "expire_in"     -> x.named("expire_in")
          )
          assert(x.named("expire_in").des[Long] <= 86400)
          assert(x.named("expire_in").des[Long] >= 86300)
      }
    }

    "to uri query literal" in {
      TokenToQuery(
        AccessToken(
          "aaa",
          Some("bbb"),
          Seq(MyScope("ccc"), MyScope("ddd")),
          Some(86400),
          now,
          Map("x" -> "xxx", "y" -> "yyy")
        )
      ) match {
        case x =>
          x shouldBe Query(
            "token_type" -> "Bearer",
            "token"      -> "aaa",
            "scope"      -> "ccc ddd",
            "expire_in"  -> x.get("expire_in").get
          )
      }
    }
  }
}
