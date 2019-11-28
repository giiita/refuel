package refuel.json.internal

private[json] object JsonCodeMap {

  sealed abstract class JsonCode(transformed: String, raws: String*)

  // RFC8259 https://www.rfc-editor.org/info/rfc8259
  private[json] final val U0009 = 0x09.toChar // \t
  private[json] final val U0020 = 0x20.toChar // \s
  private[json] final val U000A = 0x0a.toChar // \n
  private[json] final val U000D = 0x0d.toChar // \r

  private[json] final val U0022 = 0x22.toChar // "
  private[json] final val U005C = 0x5c.toChar // \

  private[json] final val U0007 = 0x07.toChar // \a
  private[json] final val U0008 = 0x08.toChar // \b
  private[json] final val U000C = 0x0c.toChar // \f

  private[json] final val OBJECT_START = 0x7b.toChar
  private[json] final val OBJECT_END = 0x7d.toChar
  private[json] final val ARRAY_START = 0x5b.toChar
  private[json] final val ARRAY_END = 0x5d.toChar

  private[json] final val UNICODE_ESCAPE = 0x75.toChar
  private[json] final val COMMA = 0x2c.toChar
  private[json] final val COLON = 0x3a.toChar

  private[json] final val WHITESPACES = Array(U0009, U0020, U000A, U000D)
  private[json] final val DELIMITATION = Array(COMMA, COLON, OBJECT_END, ARRAY_END)

  def toDescapeChar(c: Char): Char = {
    c match {
      case 't' => U0009
      case 'n' => U000A
      case 'r' => U000D

      case '"' => U0022
      case '\\' => U005C

      case 'a' => U0007
      case 'b' => U0008
      case 'f' => U000C
    }
  }
}
