package refuel.cipher

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.inject.Injector

class Base64TranscoderTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  val plain  = "和えrbぱえrぼあけろhkまえおf、^=#,"
  val base64 = "5ZKM44GIcmLjgbHjgYhy44G844GC44GR44KNaGvjgb7jgYjjgYpm44CBXj0jLA=="

  "encode" should {
    "str to str" in {
      new Base64Transcoder().encodeToStr(plain) shouldBe base64
    }
    "str to bytes" in {
      new Base64Transcoder().encodeToBytes(plain) shouldBe base64.getBytes()
    }
    "bytes to str" in {
      new Base64Transcoder().encodeToStr(plain.getBytes()) shouldBe base64
    }
    "bytes to bytes" in {
      new Base64Transcoder().encodeToBytes(plain.getBytes()) shouldBe base64.getBytes()
    }
  }

  "decode" should {
    "str to str" in {
      new Base64Transcoder().decodeToStr(base64) shouldBe plain
    }
    "str to bytes" in {
      new Base64Transcoder().decodeToBytes(base64) shouldBe plain.getBytes()
    }
    "bytes to str" in {
      new Base64Transcoder().decodeToStr(base64.getBytes()) shouldBe plain
    }
    "bytes to bytes" in {
      new Base64Transcoder().decodeToBytes(base64.getBytes()) shouldBe plain.getBytes()
    }
  }
}
