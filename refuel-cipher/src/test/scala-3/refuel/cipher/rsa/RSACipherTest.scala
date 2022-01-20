package refuel.cipher.rsa

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.cipher.CryptographyConverter
import refuel.cipher.algorithm.CryptType.RSA
import refuel.inject.Injector

import scala.util.Try

class RSACipherTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  val keypair = RSAKeyFactory.generateAuto()

  "(RAW) String => (ENC) String => (RAW) String" should {
    "Encrypt by public" in {
      val raw =
        "ア０w４雨t魔９ヲイ４jtm２３rめgfp亜wjrg９おじゃおkmアエオrb萌krMBお会え雨rhがsこdモアskjrごじゃねrとbかもfsKB真緒dfhj簿明けmrTBお会えkmンボ藍dfjぼあksmfぼあじぇおばjdふぉkbまおえkrbm"
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encryptToStr(raw, keypair.rsaPublicKey)
      val decrypt = encrypt.flatMap(cipher.decryptToStr(_, keypair.rsaPrivateKey))
      Try(raw) shouldBe decrypt
    }

    "Encrypt by private" in {
      val raw =
        (1 to 501).map(x => "X").mkString
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encryptToStr(raw, keypair.rsaPrivateKey)
      val decrypt = encrypt.flatMap(cipher.decryptToStr(_, keypair.rsaPublicKey))
      Try(raw) shouldBe decrypt
    }
  }

  "(RAW) String => (ENC) String => (RAW) Byte" should {
    "Encrypt by public" in {
      val raw =
        "ア０w４雨t魔９ヲイ４jtm２３rめgfp亜wjrg９おじゃおkmアエオrb萌krMBお会え雨rhがsこdモアskjrごじゃねrとbかもfsKB真緒dfhj簿明けmrTBお会えkmンボ藍dfjぼあksmfぼあじぇおばjdふぉkbまおえkrbm"
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encryptToStr(raw, keypair.rsaPublicKey)
      val decrypt = encrypt.flatMap(cipher.decrypt(_, keypair.rsaPrivateKey))
      Try(raw) shouldBe decrypt.map(new String(_))
    }

    "Encrypt by private" in {
      val raw =
        (1 to 501).map(x => "X").mkString
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encryptToStr(raw, keypair.rsaPrivateKey)
      val decrypt = encrypt.flatMap(cipher.decrypt(_, keypair.rsaPublicKey))
      Try(raw) shouldBe decrypt.map(new String(_))
    }
  }

  "(RAW) String => (ENC) Byte => (RAW) String" should {
    "Encrypt by public" in {
      val raw =
        "ア０w４雨t魔９ヲイ４jtm２３rめgfp亜wjrg９おじゃおkmアエオrb萌krMBお会え雨rhがsこdモアskjrごじゃねrとbかもfsKB真緒dfhj簿明けmrTBお会えkmンボ藍dfjぼあksmfぼあじぇおばjdふぉkbまおえkrbm"
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encrypt(raw, keypair.rsaPublicKey)
      val decrypt = encrypt.flatMap(cipher.decryptToStr(_, keypair.rsaPrivateKey))
      Try(raw) shouldBe decrypt
    }

    "Encrypt by private" in {
      val raw =
        (1 to 501).map(x => "X").mkString
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encrypt(raw, keypair.rsaPrivateKey)
      val decrypt = encrypt.flatMap(cipher.decryptToStr(_, keypair.rsaPublicKey))
      Try(raw) shouldBe decrypt.map(new String(_))
    }
  }

  "(RAW) Byte => (ENC) Byte => (RAW) Byte" should {
    "Encrypt by public" in {
      val raw =
        "ア０w４雨t魔９ヲイ４jtm２３rめgfp亜wjrg９おじゃおkmアエオrb萌krMBお会え雨rhがsこdモアskjrごじゃねrとbかもfsKB真緒dfhj簿明けmrTBお会えkmンボ藍dfjぼあksmfぼあじぇおばjdふぉkbまおえkrbm"
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encrypt(raw.getBytes(), keypair.rsaPublicKey)
      val decrypt = encrypt.flatMap(cipher.decrypt(_, keypair.rsaPrivateKey))
      Try(raw) shouldBe decrypt.map(new String(_))
    }

    "Encrypt by private" in {
      val raw =
        (1 to 501).map(x => "X").mkString
      val cipher = inject[CryptographyConverter[RSA]]

      val encrypt = cipher.encrypt(raw.getBytes(), keypair.rsaPrivateKey)
      val decrypt = encrypt.flatMap(cipher.decrypt(_, keypair.rsaPublicKey))
      Try(raw) shouldBe decrypt.map(new String(_))
    }
  }
}
