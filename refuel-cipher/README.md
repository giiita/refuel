# refuel-container

```
libraryDependencies += "com.phylage" %% "refuel-cipher" % "1.4.3"
````

## Usage

- application.conf
```
cipher.rsa.private = ${BASE64_ENCODED_PRIVATE_KEY}
cipher.rsa.public  = ${BASE64_ENCODED_PUBLIC_KEY}
```

```scala
class Test(rsaKey: RSAKeyPair, cipher: CryptographyConverter[RSA]) extends AutoInject {
  
  def encrypt(rawString: String): String = cipher.encryptToStr(rawString, rsaKey.rsaPublicKey)
  def decrypt(encrypted: String): String = cipher.decryptToStr(encrypted, rsaKey,rsaPrivateKey)
  
  def sign(rawString: String): String = cipher.encryptToStr(rawString, rsaKey.rsaPrivateKey)
  def verify(rawString: String): String = cipher.decryptToStr(rawString, rsaKey.rsaPrivateKey)
}
```