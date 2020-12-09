package refuel.cipher.aes

import javax.crypto.IllegalBlockSizeException
import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.cipher.CipherAlg.AES_GCM_NoPadding
import refuel.cipher.algorithm.CryptType.AES
import refuel.cipher.{CipherAlg, CryptographyConverter}
import refuel.injector.Injector

class AESCipherTest extends AsyncWordSpec with Matchers with Diagrams with Injector {

  "(RAW) String => (ENC) String => (RAW) String" should {
    "Encrypt by GCM" in {
      shade { implicit c =>
        val secret = AESKeyFactory.withGCMParam()
        new AES_GCM_NoPadding().index[CipherAlg[AES]]()
        val raw    = (1 to 1012).map(_ => "x").mkString
        val cipher = inject[CryptographyConverter[AES]]

        val encrypt = cipher.encryptToStr(raw, secret)
        val decrypt = encrypt.flatMap(cipher.decryptToStr(_, secret))
        raw shouldBe decrypt.get
      }
    }
    "Encrypt by CBC" in {
      shade { implicit c =>
        val secret = AESKeyFactory.withIvParam()
        val raw    = (1 to 16).map(_ => "x").mkString
        val cipher = inject[CryptographyConverter[AES]]

        val encrypt = cipher.encryptToStr(raw, secret)
        val decrypt = encrypt.flatMap(cipher.decryptToStr(_, secret))
        raw shouldBe decrypt.get
      }
    }
    "Failure not 16 bytes encryption by CBC" in {
      shade { implicit c =>
        intercept[IllegalBlockSizeException] {
          val secret = AESKeyFactory.withIvParam()
          val raw    = (1 to 17).map(_ => "x").mkString
          val cipher = inject[CryptographyConverter[AES]]

          val encrypt = cipher.encryptToStr(raw, secret)
          val decrypt = encrypt.flatMap(cipher.decryptToStr(_, secret))
          raw shouldBe decrypt.get
        }
      }.getMessage shouldBe "Input length not multiple of 16 bytes"
    }
  }
}
