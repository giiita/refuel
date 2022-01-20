package refuel.cipher.rsa

import refuel.cipher.algorithm.CryptType
import refuel.cipher.algorithm.CryptType.{AES, RSA}

import javax.crypto.Cipher
import refuel.cipher.{BytesTranscoder, CipherAlg, CryptographyConverter}
import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import scala.util.Try

@Inject[Finally]
class RSACipher(override val bytesTranscoder: BytesTranscoder, override val mode: CipherAlg[RSA])
    extends CryptographyConverter[RSA]
    with AutoInject {
  override val ct: RSA = RSA

  def encrypt(value: Array[Byte], key: RSA#Key): Try[Array[Byte]] = Try {
    val cipher = mode.cipher
    key.encryptionInitiate(cipher)
    bytesTranscoder.encodeToBytes(cipher.doFinal(value))
  }

  def decrypt(value: Array[Byte], key: RSA#Key): Try[Array[Byte]] = Try {
    val cipher = mode.cipher
    key.decryptionInitiate(cipher)
    cipher.doFinal(bytesTranscoder.decodeToBytes(value))
  }
}
