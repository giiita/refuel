package refuel.json.tokenize

import java.util

import refuel.json.JsonVal
import refuel.json.entry._
import refuel.json.error.IllegalJsonFormat
import refuel.json.tokenize.combinator.ExtensibleIndexWhere

import scala.annotation.{switch, tailrec}

class JsonTokenizer(rs: Array[Char]) extends ExtensibleIndexWhere(rs) {

  private[this] lazy final val Radix = 16

  override protected var pos: Int                     = 0
  private[this] var closingSyntaxCheckEnable: Boolean = false

  private[this] final def glowArray(addStrLen: Int): Unit = {
    chbuff = util.Arrays.copyOf(chbuff, Integer.highestOneBit(addStrLen) << 1)
  }

  private[this] var chbuff = new Array[Char](1 << 7)

  private def incl(x: Int = 1): Unit = {
    pos += x
  }

  override def beEOF: Unit = {
    throw new IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
  }

  @tailrec
  private[this] final def detectLiteral(len: Int): Int = {
    if (pos >= length) beEOF
    (rs(pos): @switch) match {
      case '\\' if pos + 5 <= length && rs(pos + 1) == 'u' =>
        incl(6)
        val lastLen = len + 1
        if (chbuff.length < lastLen) glowArray(len)
        chbuff(len) = Integer.parseInt(new String(rs, pos - 4, 4), Radix).toChar
        detectLiteral(lastLen)
      case '"' if rs(pos - 1) != '\\' =>
        incl()
        len
      case s =>
        incl()
        val lastLen = len + 1
        if (chbuff.length < lastLen) glowArray(len)
        chbuff(len) = s
        detectLiteral(lastLen)
    }
  }

  @tailrec
  private[this] final def detectAnyVal(len: Int): Int = {
    if (pos >= length) len
    else {
      (rs(pos): @switch) match {
        case ',' | ':' | ' ' | '}' | ']' =>
          len
        case s =>
          incl()
          val lastLen = len + 1
          if (chbuff.length < lastLen) glowArray(len)
          chbuff(len) = s
          detectAnyVal(lastLen)
      }
    }
  }

  @tailrec
  protected final def loop(rb: ResultBuff[JsonVal]): JsonVal = {
    indexWhere(_ > 32)
    val x = rs(pos)
    (x: @switch) match {
      case '"' =>
        incl()
        val len = detectLiteral(0)
        if (closingSyntaxCheckEnable) {
          loop(rb ++ JsString(new String(chbuff, 0, len)))
        } else JsString(new String(chbuff, 0, len))
      case ':' | ',' =>
        incl()
        rb.approvalSyntax(x)
        loop(rb)
      case '{' =>
        closingSyntaxCheckEnable = true
        incl()
        loop(JsStackObjects(rb))
      case '[' =>
        closingSyntaxCheckEnable = true
        incl()
        loop(JsStackArray(rb))
      case '}' | ']' =>
        incl()
        val skashed = rb.squash
        if (skashed.isSquashable) loop(skashed) else skashed
      case _ =>
        val len = detectAnyVal(0)
        if (closingSyntaxCheckEnable) {
          loop(rb ++ JsAnyVal.apply[String](new String(chbuff, 0, len)))
        } else JsAnyVal.apply[String](new String(chbuff, 0, len))
    }
  }
}
