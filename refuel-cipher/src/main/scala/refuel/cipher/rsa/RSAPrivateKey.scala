package refuel.cipher.rsa

import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

import com.typesafe.config.ConfigFactory
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject

trait RSAPrivateKey extends KEY {
  val key: PrivateKey
}

object RSAPrivateKey {

  private[this] lazy final val ConfigPath: String = "cipher.rsa.private"

  /** Load private key
    *
    * @param v base64 encoded private key literal
    * @return
    */
  def apply(v: String): RSAPrivateKey = {
    new RSAPrivateKey {
      val key: PrivateKey = build(v)
    }
  }

  private[this] def build(str: String): PrivateKey =
    KEY.RSAKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder.decode(str)))

  @Inject(Finally)
  class ConfigResourceDefPrivateKey() extends RSAPrivateKey with AutoInject {
    override val key: PrivateKey = build(
      ConfigFactory.load().getString(ConfigPath)
    )
  }
}
