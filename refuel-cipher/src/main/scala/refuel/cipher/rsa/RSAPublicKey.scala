package refuel.cipher.rsa

import com.typesafe.config.ConfigFactory
import refuel.cipher.KeyBundle
import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import java.net.URI
import java.security.spec.X509EncodedKeySpec
import java.security.{InvalidKeyException, PublicKey}
import java.util.Base64
import javax.crypto.Cipher
import scala.io.Source
import scala.util.{Failure, Try}

class RSAPublicKey private[refuel] (key: PublicKey) extends RSAKey {
  override def decryptionInitiate(cipher: Cipher): Unit = cipher.init(Cipher.DECRYPT_MODE, key)
  override def encryptionInitiate(cipher: Cipher): Unit = cipher.init(Cipher.ENCRYPT_MODE, key)
  override def serialize: String = new String(Base64.getEncoder.encode(key.getEncoded))
  def toPublicKey = key
}

object RSAPublicKey {

  private[this] lazy final val ConfigPath: String = "cipher.rsa.public"

  /** Load public key
    *
    * @param v base64 encoded public key literal
    * @return
    */
  def fromBase64Encoded(v: String): Try[RSAPublicKey] = {
    build(v).map(new RSAPublicKey(_))
  }
  def fromFile(path: URI): Try[RSAPublicKey] = {
    val buffer = Source.fromURI(path).getLines().toList
    val maybe = for {
      header <- buffer.headOption
      footer <- buffer.lastOption
    } yield {
      build(buffer.init.tail.mkString).map(new RSAPublicKey(_))
    }
    maybe.getOrElse[Try[RSAPublicKey]](
      Failure(new RuntimeException(s"Unexpected format: ${path.getPath}"))
    )
  }

  private[this] def build(str: String): Try[PublicKey] =
    Try {
      RSAKey.Factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder.decode(str)))
    }

  @Inject[Finally]
  class ConfigResourceDefPublicKey() extends RSAPublicKey(
    build(
      ConfigFactory.load().getString(ConfigPath)
    ).get
  ) with AutoInject
}
