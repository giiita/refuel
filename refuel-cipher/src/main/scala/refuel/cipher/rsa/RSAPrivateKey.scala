package refuel.cipher.rsa

import com.typesafe.config.ConfigFactory
import refuel.cipher.KeyBundle
import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import java.net.URI
import java.security.spec.PKCS8EncodedKeySpec
import java.security.{InvalidKeyException, PrivateKey}
import java.util.Base64
import javax.crypto.Cipher
import scala.io.Source
import scala.util.{Failure, Try}

class RSAPrivateKey private[refuel] (key: PrivateKey) extends RSAKey {
  override def decryptionInitiate(cipher: Cipher): Unit = cipher.init(Cipher.DECRYPT_MODE, key)
  override def encryptionInitiate(cipher: Cipher): Unit = cipher.init(Cipher.ENCRYPT_MODE, key)
  override def serialize: String = new String(Base64.getEncoder.encode(key.getEncoded))
  def toPrivateKey = key
}

object RSAPrivateKey {

  private[this] lazy final val ConfigPath: String = "cipher.rsa.private"

  /** Load private key
    *
    * @param v base64 encoded private key literal
    * @return
    */
  def fromBase64Encoded(v: String): Try[RSAPrivateKey] = {
    build(v).map(new RSAPrivateKey(_))
  }
  def fromFile(path: URI): Try[RSAPrivateKey] = {
    val buffer = Source.fromURI(path).getLines().toList
    val maybe = for {
      header <- buffer.headOption
      footer <- buffer.lastOption
    } yield {
        build(buffer.init.tail.mkString).map(new RSAPrivateKey(_))
    }
    maybe.getOrElse[Try[RSAPrivateKey]](
      Failure(new RuntimeException(s"Unexpected format: ${path.getPath}"))
    )
  }

  private[this] def build(str: String): Try[PrivateKey] =
    Try {
      RSAKey.Factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder.decode(str)))
    }
  @Inject[Finally]
  class ConfigResourceDefPrivateKey() extends RSAPrivateKey(
    build(
      ConfigFactory.load().getString(ConfigPath)
    ).get
  ) with AutoInject
}
