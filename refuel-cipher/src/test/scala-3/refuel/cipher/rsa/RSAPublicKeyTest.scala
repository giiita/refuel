package refuel.cipher.rsa

import org.scalatest.diagrams.Diagrams
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import refuel.inject.Injector

class RSAPublicKeyTest extends AsyncWordSpec with Matchers with Diagrams with Injector {
  "fromBase64Encoded" should {
    "pkcs8" in {
      val encoded = Seq(
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt7KRYltNUCbH5vubam+H",
        "diTUylt/PP76wMKmCz8F0PSDCCNRYaQTtUPn1X8J+irtd3scg+vI8LvydE4nGWhc",
        "1NBOe8AQ7Ts6mASxvbfO1VZS//8LwytC9SETyZuOSWHVGi0N4mneVp84vXOqBiD2",
        "2GB8PkHoAsnSnrTAEESh+9hYmyF3H/gRY5lcGuBsXZ1oEC4Sy90Z01UKLdDaYWi6",
        "WyseeVfFF3G8nKOEHuaA3lNDK8tETu7Al341KyL/Krv1VFBJmzoXTLh6+Ttn6pnR",
        "sGt2ksnZYWzow3V+lZz0O/IEKbnYGjf2jGJ6F+Z0kCjYiYGdmynMQGypo8vP7utq",
        "IwIDAQAB",
      ).mkString
      val result = RSAPublicKey.fromBase64Encoded(encoded)
      result.map { key =>
        key.serialize shouldBe encoded
      } getOrElse fail()
    }
  }

  "fromFile" should {
    "pkcs8" in {
      val encoded = Seq(
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt7KRYltNUCbH5vubam+H",
        "diTUylt/PP76wMKmCz8F0PSDCCNRYaQTtUPn1X8J+irtd3scg+vI8LvydE4nGWhc",
        "1NBOe8AQ7Ts6mASxvbfO1VZS//8LwytC9SETyZuOSWHVGi0N4mneVp84vXOqBiD2",
        "2GB8PkHoAsnSnrTAEESh+9hYmyF3H/gRY5lcGuBsXZ1oEC4Sy90Z01UKLdDaYWi6",
        "WyseeVfFF3G8nKOEHuaA3lNDK8tETu7Al341KyL/Krv1VFBJmzoXTLh6+Ttn6pnR",
        "sGt2ksnZYWzow3V+lZz0O/IEKbnYGjf2jGJ6F+Z0kCjYiYGdmynMQGypo8vP7utq",
        "IwIDAQAB",
      ).mkString
      val result = RSAPublicKey.fromFile(getClass.getClassLoader.getResource("pkcs8-pub.pem").toURI)
      result.map { key =>
        key.serialize shouldBe encoded
      }.get
    }
  }
}
