package refuel.cipher.algorithm

import refuel.cipher.KeyBundle
import refuel.cipher.aes.AESKey
import refuel.cipher.rsa.RSAKey

sealed trait CryptType {
  type Key <: KeyBundle
}

object CryptType {
  trait RSA extends CryptType {
    type Key = RSAKey
  }
  case object RSA extends RSA
  trait AES extends CryptType {
    type Key = AESKey
  }
  case object AES extends AES
}
