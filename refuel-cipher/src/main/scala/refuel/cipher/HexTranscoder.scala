package refuel.cipher

class HexTranscoder extends BytesTranscoder {
  def encodeToBytes(bytes: Array[Byte]): Array[Byte] = byteToHex(bytes)

  /** Convert bytes to hex string.
    *
    * @param bytes Byte array
    * @return Hex formatted string
    */
  private[this] final def byteToHex(bytes: Array[Byte]): Array[Byte] = {
    val buf = new StringBuilder()
    bytes.foreach { byte =>
      buf.append(Character.forDigit((byte >> 4) & 0xF, 16))
      buf.append(Character.forDigit(byte & 0xF, 16))
    }
    buf.toString().getBytes()
  }

  def decodeToBytes(hex: Array[Byte]): Array[Byte] = hexToByte(hex)

  /** Convert hex string to byte array.
    *
    * @param value hex formatted string
    * @return byte array
    */
  private[this] final def hexToByte(value: Array[Byte]): Array[Byte] = {
    _hexToBytes(value, new scala.collection.mutable.ArrayBuffer[Byte](value.length / 2))
  }

  @scala.annotation.tailrec
  private[this] final def _hexToBytes(
      value: Array[Byte],
      buf: scala.collection.mutable.ArrayBuffer[Byte],
      i: Int = 0
  ): Array[Byte] = {
    if (i == value.length) {
      buf.toArray
    } else {
      buf += ((toDigit(value(i).toChar) << 4) + toDigit(value(i + 1).toChar)).toByte
      _hexToBytes(value, buf, i + 2)
    }
  }

  private[this] final def toDigit(hexChar: Char) = {
    val digit = Character.digit(hexChar, 16)
    if (digit == -1) throw new IllegalArgumentException("Invalid Hexadecimal Character: " + hexChar)
    digit
  }
}
