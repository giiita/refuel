package refuel.cipher

import refuel.inject.InjectionPriority.Finally
import refuel.inject.{AutoInject, Inject}

import java.util.Base64

@Inject[Finally]
class Base64Transcoder extends BytesTranscoder with AutoInject {
  def encodeToBytes(bytes: Array[Byte]): Array[Byte] = Base64.getEncoder.encode(bytes)

  def decodeToBytes(hex: Array[Byte]): Array[Byte] = Base64.getDecoder.decode(hex)
}
