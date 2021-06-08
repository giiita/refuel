package refuel.cipher.rsa

import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import com.typesafe.config.ConfigFactory
import refuel.domination.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

trait RSAPublicKey extends KEY {
  override val key: PublicKey

}
object RSAPublicKey {

  private[this] lazy final val ConfigPath: String = "cipher.rsa.public"

  /** Load public key
    *
    * @param v base64 encoded public key literal
    * @return
    */
  def apply(v: String): RSAPublicKey = {
    new RSAPublicKey {
      override val key: PublicKey =
        KEY.RSAKeyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder.decode(v)))
    }
  }

  @Inject[Finally]
  class ConfigResourceDefPublicKey() extends RSAPublicKey with AutoInject {
    override val key: PublicKey = build(
      ConfigFactory.load().getString(ConfigPath)
    )
  }

  private[this] def build(str: String): PublicKey =
    KEY.RSAKeyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder.decode(str)))
}
