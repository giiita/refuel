package refuel.cipher

trait BytesTranscoder {
  def encodeToBytes(byte: String): Array[Byte] = encodeToBytes(byte.getBytes())
  def encodeToBytes(bytes: Array[Byte]): Array[Byte]
  def encodeToStr(byte: String): String = new String(encodeToBytes(byte))
  def encodeToStr(bytes: Array[Byte]): String = {
    println(bytes.toSeq)
    new String(encodeToBytes(bytes))
  }

  def decodeToBytes(hex: Array[Byte]): Array[Byte]
  def decodeToBytes(hexString: String): Array[Byte] = decodeToBytes(hexString.getBytes())
  def decodeToStr(hex: Array[Byte]): String         = new String(decodeToBytes(hex))
  def decodeToStr(hexString: String): String        = new String(decodeToStr(hexString.getBytes))
}
