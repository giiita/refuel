package refuel.json.tokenize.encoded

import refuel.json.tokenize.JsonTokenizer

import scala.annotation.switch

class EncodedJsonTokenizer(rs: Array[Char]) extends JsonTokenizer(rs) {
  private[this] final def fromEscaping(c: Char): Char = {
    (c: @switch) match {
      case 'r'   => '\r'
      case 'n'   => '\n'
      case 'f'   => '\f'
      case 'b'   => '\b'
      case 't'   => '\t'
      case other => other
    }
  }

  override protected def encodedLiteralHandle(len: Int): Int = {
    if (pos + 5 <= length && rs(pos + 1) == 'u') {
      incl(6)
      val lastLen = len + 1
      if (chbuff.length < lastLen) glowArray(len)
      chbuff(len) = Integer.parseInt(new String(rs, pos - 4, 4), Radix).toChar
      lastLen
    } else if (pos + 1 <= length) {
      incl(2)
      val lastLen = len + 1
      if (chbuff.length < lastLen) glowArray(len)
      if (rs(pos - 1) == '\\') {
        chbuff(len) = fromEscaping(rs(pos))
        incl()
        lastLen
      } else {
        chbuff(len) = fromEscaping(rs(pos - 1))
        lastLen
      }
    } else beEOF
  }
}
