package com.phylage.scaladia.cipher

import com.phylage.scaladia.json.Codec

abstract class Encrypted[T: Codec] {
  type Decryptor

  def decrypt(key: Decryptor): T
}

object Encrypted {
  type Decoder[T] = {
    def decode(v: String): T
  }
}