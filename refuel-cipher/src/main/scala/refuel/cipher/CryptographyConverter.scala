package refuel.cipher

import refuel.cipher.algorithm.CryptType
import refuel.injector.AutoInject

import scala.util.Try

trait CryptographyConverter[T <: CryptType] extends AutoInject {
  val bytesTranscoder: BytesTranscoder
  val mode: CipherAlg[T]

  def encryptToStr(value: String, key: T#Key): Try[String] = {
    encryptToStr(value.getBytes, key)
  }

  def encryptToStr(value: Array[Byte], key: T#Key): Try[String] = {
    encrypt(value, key).map(new String(_))
  }

  def encrypt(value: String, key: T#Key): Try[Array[Byte]] = {
    encrypt(value.getBytes, key)
  }

  def encrypt(value: Array[Byte], key: T#Key): Try[Array[Byte]]

  def decryptToStr(value: String, key: T#Key): Try[String] = {
    decryptToStr(value.getBytes(), key)
  }

  def decryptToStr(value: Array[Byte], key: T#Key): Try[String] = {
    decrypt(value, key).map(new String(_))
  }

  def decrypt(value: String, key: T#Key): Try[Array[Byte]] = {
    decrypt(value.getBytes(), key)
  }

  def decrypt(value: Array[Byte], key: T#Key): Try[Array[Byte]]
}
