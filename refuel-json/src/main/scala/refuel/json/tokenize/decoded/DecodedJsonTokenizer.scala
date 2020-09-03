package refuel.json.tokenize.decoded

import refuel.json.tokenize.JsonTokenizer

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
      chbuff(len) = '\\'
      len + 1
    }
  }
}
