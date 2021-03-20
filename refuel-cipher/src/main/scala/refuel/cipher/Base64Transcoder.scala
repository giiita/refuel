package refuel.cipher

import java.util.Base64

import refuel.domination.Inject
import refuel.domination.InjectionPriority.Finally
import refuel.injector.AutoInject

@Inject[Finally]
class Base64Transcoder extends BytesTranscoder with AutoInject {
  def encodeToBytes(bytes: Array[Byte]): Array[Byte] = Base64.getEncoder.encode(bytes)

  def decodeToBytes(hex: Array[Byte]): Array[Byte] = Base64.getDecoder.decode(hex)
}
