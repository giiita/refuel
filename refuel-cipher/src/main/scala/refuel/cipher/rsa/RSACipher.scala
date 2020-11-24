package refuel.cipher.rsa

import javax.crypto.Cipher
import refuel.cipher.algorithm.CryptType.RSA
import refuel.cipher.{BytesTranscoder, CipherAlg, CryptographyConverter}
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject

import scala.util.Try

@Inject(Finally)
class RSACipher(override val bytesTranscoder: BytesTranscoder, override val mode: CipherAlg[RSA])
    extends CryptographyConverter[RSA]
    with AutoInject {

  def encrypt(value: Array[Byte], key: RSA#Key): Try[Array[Byte]] = Try {
    val cipher = mode.cipher
    cipher.init(Cipher.ENCRYPT_MODE, key.key)
    bytesTranscoder.encodeToBytes(cipher.doFinal(value))
  }

  def decrypt(value: Array[Byte], key: RSA#Key): Try[Array[Byte]] = Try {
    val cipher = mode.cipher
    cipher.init(Cipher.DECRYPT_MODE, key.key)
    cipher.doFinal(bytesTranscoder.decodeToBytes(value))
  }
}
