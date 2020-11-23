package refuel.cipher.aes

import java.security.Key
import java.util.Base64

import com.typesafe.config.ConfigFactory
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import refuel.cipher.rsa.KEY
import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject

trait AESKey extends KEY {
  val key: Key
  val iv: IvParameterSpec

  override def serialize: String =
    s"${new String(Base64.getEncoder.encode(key.getEncoded))} ${new String(Base64.getEncoder.encode(iv.getIV))}"
}

object AESKey {

  private[this] lazy final val KeyPath: String = "cipher.aes.key"
  private[this] lazy final val IvPath: String  = "cipher.aes.initialization-vector"

  @Inject(Finally)
  class ConfigResourceDefPublicKey() extends AESKey with AutoInject {
    override val key: Key = new SecretKeySpec(Base64.getDecoder.decode(conf.getString(KeyPath).getBytes()), "AES")
    override val iv: IvParameterSpec = new IvParameterSpec(
      Base64.getDecoder.decode(conf.getString(IvPath).getBytes())
    )
    val conf = ConfigFactory.load()
  }

}
