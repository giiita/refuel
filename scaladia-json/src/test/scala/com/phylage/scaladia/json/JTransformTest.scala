package com.phylage.scaladia.json

import com.phylage.scaladia.json.error.IllegalJsonFormat
import org.scalatest.{AsyncWordSpec, DiagrammedAssertions, Matchers}

class JTransformTest extends AsyncWordSpec with Matchers with DiagrammedAssertions with JTransform {
  "Json tree build" should {
    "fail case - EOF position" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":123"""".jsonTree
      }.getMessage shouldBe "EOF in an unexpected position."
    }
    "fail case - Unexpected final json tree" in {
      intercept[IllegalJsonFormat] {
        s"""{"value": }""".jsonTree
      }.getMessage shouldBe "The conversion was successful, but the generated JsonTree is invalid.\n{\"value\": }"
    }
    "fail case - EOF position 2" in {
      intercept[IllegalJsonFormat] {
        s"""{"value":"3}""".jsonTree
      }.getMessage shouldBe "EOF in an unexpected position.\n{\"value\":\"<ERROR FROM>3}"
    }
  }
}
