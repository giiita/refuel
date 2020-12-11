package refuel.cipher.aes

import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import java.util.Base64

import com.typesafe.config.{Config, ConfigFactory}
import javax.crypto.spec.{GCMParameterSpec, IvParameterSpec, SecretKeySpec}
import refuel.cipher.rsa.KEY
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject

import scala.util.Try

case class AESKey(key: Key, paramSpec: Option[AlgorithmParameterSpec]) extends KEY { me =>
  def withIvParamSpec(_iv: IvParameterSpec): AESKey   = copy(paramSpec = Some(_iv))
  def withGCMParamSpec(gcm: GCMParameterSpec): AESKey = copy(paramSpec = Some(gcm))

  override def serialize: String =
    s"KEY: ${new String(Base64.getEncoder.encode(key.getEncoded))}\nSPEC: ${paramSpec
      .map {
        case x: GCMParameterSpec => x.getIV
        case x: IvParameterSpec  => x.getIV
      }
      .map(x => new String(Base64.getEncoder.encode(x)))}"
}

object AESKey {

  private[this] lazy final val KeyPath: String = "cipher.aes.key"

  private[this] lazy final val IvPath: String = "cipher.aes.iv"

  private[this] lazy final val GCMLengthPath: String = "cipher.aes.gcm.len"
  private[this] lazy final val GCMBytePath: String   = "cipher.aes.gcm.byte"

  private[this] lazy final val key: Key =
    new SecretKeySpec(Base64.getDecoder.decode(conf.getString(KeyPath).getBytes()), "AES")
  private[this] lazy final val paramSpec: Option[AlgorithmParameterSpec] =
    Try(conf.getString(IvPath)).toOption
      .map(_.getBytes())
      .map(Base64.getDecoder.decode)
      .map(new IvParameterSpec(_))
      .orElse(
        for {
          len   <- Try(conf.getInt(GCMLengthPath)).toOption
          bytes <- Try(conf.getString(GCMBytePath)).toOption
        } yield new GCMParameterSpec(len, Base64.getDecoder.decode(bytes.getBytes()))
      )
  private[this] lazy final val conf: Config = ConfigFactory.load()

  @Inject(Finally)
  class ConfigResourceDefPublicKey() extends AESKey(key, paramSpec) with AutoInject {}

}
