package refuel.cipher

import refuel.cipher.algorithm.CryptType
import refuel.cipher.algorithm.CryptType.{AES, RSA}
import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import javax.crypto.Cipher

trait CipherAlg[T <: CryptType] extends Serializable {
  def mode: String
  def cipher: Cipher = Cipher.getInstance(mode)
}

object CipherAlg {
  @Inject[Finally]
  class RSA_ECB_PKCS1Padding extends CipherAlg[RSA] with AutoInject {
    override final def mode: String = "RSA/ECB/PKCS1Padding"
  }
  class RSA_ECB_OAEPWithSHA_1AndMGF1Padding extends CipherAlg[RSA] {
    override final def mode: String = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding"
  }
  class RSA_ECB_OAEPWithSHA_256AndMGF1Padding extends CipherAlg[RSA] {
    override final def mode: String = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
  }

  @Inject[Finally]
  class AES_CBC_NoPadding extends CipherAlg[AES] with AutoInject {
    override final def mode: String = "AES/CBC/NoPadding"
  }
  class AES_GCM_NoPadding extends CipherAlg[AES] {
    override final def mode: String = "AES/GCM/NoPadding"
  }
}
