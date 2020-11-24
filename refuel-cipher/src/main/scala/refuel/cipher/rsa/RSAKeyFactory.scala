package refuel.cipher.rsa

import java.security.{KeyPairGenerator, PrivateKey, PublicKey, SecureRandom}

case class RSAKeyPair(rsaPublicKey: RSAPublicKey, rsaPrivateKey: RSAPrivateKey) {}

object RSAKeyFactory {
  def generateAuto(keyLen: Int = 4096, ran: SecureRandom = SecureRandom.getInstanceStrong): RSAKeyPair = {

    val kpg = KeyPairGenerator.getInstance("RSA")
    kpg.initialize(keyLen, ran)
    val pair = kpg.generateKeyPair()

    RSAKeyPair(
      new RSAPublicKey {
        override val key: PublicKey = pair.getPublic
      },
      new RSAPrivateKey {
        override val key: PrivateKey = pair.getPrivate
      }
    )
  }
}
