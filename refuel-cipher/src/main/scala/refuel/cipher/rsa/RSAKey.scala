package refuel.cipher.rsa

import refuel.cipher.KeyBundle

import java.security.KeyFactory
import java.util.Base64

object RSAKey {
  private[refuel] lazy final val Factory = KeyFactory.getInstance("RSA")
}
trait RSAKey extends KeyBundle
