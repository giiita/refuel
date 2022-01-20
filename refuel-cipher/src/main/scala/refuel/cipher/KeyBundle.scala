package refuel.cipher

import java.security.{Key, KeyFactory}
import java.util.Base64
import javax.crypto.{Cipher, KeyGenerator}

trait KeyBundle {
  def encryptionInitiate(cipher: Cipher): Unit
  def decryptionInitiate(cipher: Cipher): Unit
  def serialize: String
}
