package refuel.cipher

import refuel.json.Codec

abstract class Encrypted[T: Codec] {
  type Decryptor

  def decrypt(key: Decryptor): T
}

object Encrypted {
  type Decoder[T] = {
    def decode(v: String): T
  }
}