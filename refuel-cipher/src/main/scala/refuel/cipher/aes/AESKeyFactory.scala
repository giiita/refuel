package refuel.cipher.aes

import java.security.{Key, SecureRandom}

import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

object AESKeyFactory {

  def generateAuto(keyLen: Int = 256, ran: SecureRandom = new SecureRandom()): AESKey = {

    val kpg = KeyGenerator.getInstance("AES")
    kpg.init(keyLen, ran)

    new AESKey {
      override val key: Key = kpg.generateKey()
      override val iv: IvParameterSpec = new IvParameterSpec({
        val arr = new Array[Byte](1 << 4)
        new SecureRandom().nextBytes(arr)
        arr
      })
    }
  }
}
