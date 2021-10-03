package refuel.json

object JsStringEscape {
  def main(args: Array[String]): Unit = {

    val HC = "0123456789ABCDEF".toCharArray
    val sOutputEscapes128: Array[Int] = {
      val table = new Array[Int](128)
      // Control chars need generic escape sequence
      for (i <- 0 until 32) { // 04-Mar-2011, tatu: Used to use "-(i + 1)", replaced with constant
        table(i) = CharacterEscapes.ESCAPE_STANDARD
      }
      // Others (and some within that range too) have explicit shorter sequences
      table('"') = '"'
      table('\\') = '\\'
      // Escaping of slash is optional, so let's not add it
      table(0x08) = 'b'
      table(0x09) = 't'
      table(0x0C) = 'f'
      table(0x0A) = 'n'
      table(0x0D) = 'r'
      table
    }

    def esc(value: Int): Array[Char] = {
      val qbuf = new Array[Char](6)
      qbuf(0) = '\\'
      qbuf(1) = 'u'
      qbuf(2) = '0'
      qbuf(3) = '0'
      // We know it's a control char, so only the last 2 chars are non-0
      qbuf(4) = HC(value >> 4)
      qbuf(5) = HC(value & 0xF)
      qbuf
    }

    def v(value: Int): Any = {
      if (sOutputEscapes128(value) > 0) esc(value).toList else value
    }
    println(v('"'))
  }
}

object CharacterEscapes {
  final val ESCAPE_STANDARD = -1
}