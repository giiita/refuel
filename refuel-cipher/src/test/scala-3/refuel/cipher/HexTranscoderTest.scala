package refuel.cipher

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.inject.Injector

class HexTranscoderTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  val plain     = "和えrbぱえrぼあけろhkまえおf、^=#,"
  val encrypted = "e5928ce381887262e381b1e3818872e381bce38182e38191e3828d686be381bee38188e3818a66e380815e3d232c"

  "encode" should {
    "str to str" in {
      new HexTranscoder().encodeToStr(plain) shouldBe encrypted
    }
    "str to bytes" in {
      new HexTranscoder().encodeToBytes(plain) shouldBe encrypted.getBytes()
    }
    "bytes to str" in {
      new HexTranscoder().encodeToStr(plain.getBytes()) shouldBe encrypted
    }
    "bytes to bytes" in {
      new HexTranscoder().encodeToBytes(plain.getBytes()) shouldBe encrypted.getBytes()
    }
  }

  "decode" should {
    "str to str" in {
      new HexTranscoder().decodeToStr(encrypted) shouldBe plain
    }
    "str to bytes" in {
      new HexTranscoder().decodeToBytes(encrypted) shouldBe plain.getBytes()
    }
    "bytes to str" in {
      new HexTranscoder().decodeToStr(encrypted.getBytes()) shouldBe plain
    }
    "bytes to bytes" in {
      new HexTranscoder().decodeToBytes(encrypted.getBytes()) shouldBe plain.getBytes()
    }
  }
}
