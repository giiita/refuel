# refuel-container

```
libraryDependencies += "com.phylage" %% "refuel-cipher" % "2.0.2"
````

## Usage

- application.conf
```
cipher.rsa.private = ${BASE64_ENCODED_PRIVATE_KEY}
cipher.rsa.public  = ${BASE64_ENCODED_PUBLIC_KEY}
```

```
cipher.aes.key = ${BASE64_ENCODED_AES_KEY}
# (optional) CBC initiation vector
cipher.aes.iv = ${BASE64_ENCODED_CBC_PARAM_SPEC_BYTES}
# (optional) GCM patameter specs
cipher.aes.gcm.len = ${GCM_PARAM_SPEC_KEYLENGTH}
cipher.aes.gcm.byte = ${BASE64_ENCODED_GCM_PARAM_SPEC_BYTES}
```

```scala
class Test(rsaKey: RSAKeyPair, cipher: CryptographyConverter[RSA]) extends AutoInject {
  
  def encrypt(rawString: String): String = cipher.encryptToStr(rawString, rsaKey.rsaPublicKey)
  def decrypt(encrypted: String): String = cipher.decryptToStr(encrypted, rsaKey,rsaPrivateKey)
  
  def sign(rawString: String): String = cipher.encryptToStr(rawString, rsaKey.rsaPrivateKey)
  def verify(rawString: String): String = cipher.decryptToStr(rawString, rsaKey.rsaPrivateKey)
}
```



## How to generate encoded keys

### AES (CBC NoPadding)

```scala
object Main extends App {
  val key = AESKeyFactory.withIvParam()
  println(
    key.serialize
  )
}
```

### AES (GCM NoPadding)

```scala
object Main extends App {
  val key = AESKeyFactory.withGCMParam()
  println(
    key.serialize
  )
}
```

### RSA

```scala
object Main extends App {
  val keyPair = RSAKeyFactory.generateAuto()
  println(
    keyPair.rsaPublicKey.serialize
  )
}
```