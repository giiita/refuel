package refuel.json.tokenize

import java.util

import refuel.json.Json
import refuel.json.entry._
import refuel.json.tokenize.combinator.ExtensibleIndexWhere

import scala.annotation.{switch, tailrec}

class JTokenizer(rs: Array[Char]) extends ExtensibleIndexWhere(rs) {

  override protected var pos: Int = 0

  private[this] final def glowArray(addStrLen: Int): Unit = {
    chbuff = util.Arrays.copyOf(chbuff, Integer.highestOneBit(addStrLen) << 1)
  }

  private[this] var chbuff = new Array[Char](1 << 7)

  private def incl: Unit = {
    pos += 1
  }

  @tailrec
  private[this] final def detectLiteral(len: Int): Int = {
    if (pos >= length) throwUnexpectedEOF
    (rs(pos): @switch) match {
      case '"' =>
        incl
        len
      case s =>
        incl
        val lastLen = len + 1
        if (chbuff.length < lastLen) glowArray(len)
        chbuff(len) = s
        detectLiteral(lastLen)
    }
  }

  @tailrec
  private[this] final def detectAnyVal(len: Int): Int = {
    if (pos >= length) throwUnexpectedEOF
    (rs(pos): @switch) match {
      case ',' | ':' | ' ' | '}' | ']' =>
        len
      case s =>
        incl
        val lastLen = len + 1
        if (chbuff.length < lastLen) glowArray(len)
        chbuff(len) = s
        detectAnyVal(lastLen)
    }
  }

  @tailrec
  protected final def loop(rb: ResultBuff[Json]): Json = {
    indexWhere(_ > 32)
    val x = rs(pos)
    (x: @switch) match {
      case '"' =>
        incl
        val len = detectLiteral(0)
        loop(rb ++ JsString(new String(chbuff, 0, len)))
      case ':' | ',' =>
        incl
        loop(rb)
      case '{' =>
        incl
        loop(JsStackObjects(rb))
      case '[' =>
        incl
        loop(JsStackArray(rb))
      case '}' | ']' =>
        incl
        val skashed = rb.squash
        if (skashed.isSquashable) loop(skashed) else skashed
      case _ =>
        val len = detectAnyVal(0)
        loop(rb ++ JsAnyVal(new String(chbuff, 0, len)))
    }
  }
}
