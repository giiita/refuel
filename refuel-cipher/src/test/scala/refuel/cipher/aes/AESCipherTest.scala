package refuel.cipher.aes

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.cipher.CryptographyConverter
import refuel.cipher.algorithm.CryptType.AES
import refuel.injector.Injector

import scala.util.Try

class AESCipherTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  val secret = AESKeyFactory.generateAuto()

  "(RAW) String => (ENC) String => (RAW) String" should {
    "Encrypt by public" in {
      val raw    = (1 to 16).map(_ => "x").mkString
      val cipher = inject[CryptographyConverter[AES]]

      val encrypt = cipher.encryptToStr(raw, secret)
      val decrypt = encrypt.flatMap(cipher.decryptToStr(_, secret))
      Try(raw) shouldBe decrypt
    }
  }
}
