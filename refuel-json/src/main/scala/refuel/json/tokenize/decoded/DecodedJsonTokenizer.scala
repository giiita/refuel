package refuel.json.tokenize.decoded

import refuel.json.exception.IllegalJsonFormatException
import refuel.json.tokenize.JsonTokenizer

import scala.annotation.switch

class DecodedJsonTokenizer(rs: Array[Char]) extends JsonTokenizer(rs) {
  override protected def encodedLiteralHandle(len: Int): Int = {
    if (pos + 5 <= length && rs(pos + 1) == 'u') {
      incl(6)
      val lastLen = len + 1
      if (chbuff.length < lastLen) glowArray(len)
      chbuff(len) = Integer.parseInt(new String(rs, pos - 4, 4), Radix).toChar
      lastLen
    } else {
      incl()
      if (pos <= length) {
        if (chbuff.length <= len) glowArray(len)
        chbuff(len) = fromEscaping(rs(pos))
        incl()
        len + 1
      } else beEOF
    }
  }

  private[this] final def fromEscaping(c: Char): Char = {
    (c: @switch) match {
      case 'r'   => '\r'
      case 'n'   => '\n'
      case 'f'   => '\f'
      case 'b'   => '\b'
      case 't'   => '\t'
      case '/'   => '/'
      case '\\'  => '\\'
      case '"'   => '"'
      case other => throw IllegalJsonFormatException(s"Illegal json format: \\$other")
    }
  }
}
