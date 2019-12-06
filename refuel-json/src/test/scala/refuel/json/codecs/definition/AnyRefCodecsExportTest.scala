package refuel.json.codecs.definition

import refuel.json.{Codec, JsContext}
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

class AnyRefCodecsExportTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JsContext {
  "Any ref codec auto generation" should {
    "Expected to be compileable without explicit Codec[_] generation" in {
      implicitly[Codec[Map[String, Option[List[Seq[Vector[Array[Set[(String, String)]]]]]]]]]
      succeed
    }
  }
}
