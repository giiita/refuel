package refuel.json.codecs.definition

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.json.{Codec, JsContext}

class AnyRefCodecsExportTest extends AsyncWordSpec with Matchers with Diagrams with JsContext {
  "Any ref codec auto generation" should {
    "Expected to be compileable without explicit Codec[_] generation" in {
      implicitly[Codec[Map[String, Option[List[Seq[Vector[Array[Set[(String, String)]]]]]]]]]
      succeed
    }
  }
}
