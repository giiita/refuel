package refuel.oauth

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.injector.Injector

class Test extends AsyncWordSpec with Matchers with Diagrams with Injector {
  "DI test" should {
    "," in {
      assert {
        inject[OAuth2[String]]._provide.isInstanceOf[OAuth2[String]]
      }
    }
  }
}
