package refuel.cipher.algorithm

import refuel.cipher.aes.AESKey
import refuel.cipher.rsa.KEY

sealed trait CryptType {
  type Key <: KEY
}

object CryptType {
  class RSA extends CryptType {
    type Key = KEY
  }
  class AES extends CryptType {
    type Key = AESKey
  }
}
