package com.phylage.scaladia.json.codecs.definition

import com.phylage.scaladia.json.{Codec, JTransform}
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

class AnyRefCodecsImplTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JTransform {
  "Any ref codec auto generation" should {
    "Expected to be compileable without explicit Codec[_] generation" in {
      implicitly[Codec[Map[String, Option[List[Seq[Vector[Array[Set[(String, String)]]]]]]]]]
      succeed
    }
  }
}
