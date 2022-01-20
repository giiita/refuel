package refuel.cipher.aes

import refuel.cipher.algorithm.CryptType
import refuel.cipher.algorithm.CryptType.AES
import refuel.cipher.{BytesTranscoder, CipherAlg, CryptographyConverter}
import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import javax.crypto.Cipher
import scala.util.Try

@Inject[Finally]
class AESCipher(override val bytesTranscoder: BytesTranscoder, override val mode: CipherAlg[AES])
    extends CryptographyConverter[AES]
    with AutoInject {
  override val ct: AES = AES

  def encrypt(value: Array[Byte], key: AES#Key): Try[Array[Byte]] = Try {
    val cipher = mode.cipher
    key.encryptionInitiate(cipher)
    bytesTranscoder.encodeToBytes(cipher.doFinal(value))
  }

  def decrypt(value: Array[Byte], key: AES#Key): Try[Array[Byte]] = Try {
    val cipher = mode.cipher
    key.decryptionInitiate(cipher)
    cipher.doFinal(bytesTranscoder.decodeToBytes(value))
  }
}
