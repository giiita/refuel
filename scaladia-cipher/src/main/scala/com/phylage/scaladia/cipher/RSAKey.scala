package com.phylage.scaladia.cipher

import java.security.KeyFactory
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.util.Base64

trait RSAKey {
  val key: java.security.Key
}

object RSAKey {

  private[this] val factory = KeyFactory.getInstance("RSA")

  private[this] def decode(v: String): Array[Byte] = Base64.getDecoder.decode(v)

  object RSAPublicKey {
    def apply(v: String): RSAPublicKey = {
      new RSAPublicKey(
        factory.generatePublic(new X509EncodedKeySpec(decode(v)))
      )
    }
  }

  object RSAPrivateKey {
    def apply(v: String): RSAPrivateKey = {
      new RSAPrivateKey(
        factory.generatePrivate(new PKCS8EncodedKeySpec(decode(v)))
      )
    }
  }

}

