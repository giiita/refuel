package refuel.cipher.aes

import java.security.SecureRandom

import javax.crypto.KeyGenerator
import javax.crypto.spec.{GCMParameterSpec, IvParameterSpec}

object AESKeyFactory {
  private def Generator = KeyGenerator.getInstance("AES")

  def generateAuto(
      keyLen: Int = 256,
      ran: SecureRandom = new SecureRandom()
  ): AESKey = {
    val kpg = Generator
    kpg.init(keyLen, ran)

    AESKey(kpg.generateKey(), None)
  }

  def withIvParam(keyLen: Int = 256, ran: SecureRandom = new SecureRandom(), randLen: Int = 1 << 4): AESKey = {
    val kpg = Generator
    kpg.init(keyLen, ran)

    AESKey(
      kpg.generateKey(),
      Some(
        new IvParameterSpec({
          val arr = new Array[Byte](randLen)
          new SecureRandom().nextBytes(arr)
          arr
        })
      )
    )
  }

  def withGCMParam(keyLen: Int = 256, ran: SecureRandom = new SecureRandom(), randLen: Int = 1 << 7): AESKey = {
    val kpg = Generator
    kpg.init(keyLen, ran)

    AESKey(
      kpg.generateKey(),
      Some(
        new GCMParameterSpec(randLen, {
          val arr = new Array[Byte](randLen)
          new SecureRandom().nextBytes(arr)
          arr
        })
      )
    )
  }
}
