package refuel.cipher.rsa

import java.security.{Key, KeyFactory}
import java.util.Base64

object KEY {
  private[refuel] lazy final val RSAKeyFactory = KeyFactory.getInstance("RSA")
}
trait KEY {
  val key: Key
  def serialize: String = new String(Base64.getEncoder.encode(key.getEncoded))
}
