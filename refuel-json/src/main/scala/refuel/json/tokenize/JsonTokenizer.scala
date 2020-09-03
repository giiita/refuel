package refuel.json.tokenize

import java.util

import refuel.json.JsonVal
import refuel.json.entry._
import refuel.json.error.IllegalJsonFormat
import refuel.json.tokenize.strategy.JTransformStrategy

import scala.annotation.{switch, tailrec}

abstract class JsonTokenizer(rs: Array[Char]) extends JTransformStrategy(rs) {

  protected def encodedLiteralHandle(len: Int): Int

  protected lazy final val Radix = 16

  private[this] var closingSyntaxCheckEnable: Boolean = false

  protected final def glowArray(addStrLen: Int): Unit = {
    chbuff = util.Arrays.copyOf(chbuff, Integer.highestOneBit(addStrLen) << 1)
  }

  protected var chbuff = new Array[Char](1 << 7)

  protected def incl(x: Int = 1): Unit = {
    pos += x
  }

  override def beEOF: Int = {
    throw new IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
  }

  @tailrec
  private[this] final def detectLiteral(len: Int): Int = {
    if (pos >= length) beEOF
    (rs(pos): @switch) match {
      case '\\' =>
        detectLiteral(encodedLiteralHandle(len))
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
  protected final def tokenize(rb: ResultBuff[JsonVal]): JsonVal = {
    indexWhere(_ > 32)
    val x = rs(pos)
    (x: @switch) match {
      case '"' =>
        incl()
        val len = detectLiteral(0)
        if (closingSyntaxCheckEnable) {
          tokenize(rb ++ JsString(new String(chbuff, 0, len)))
        } else JsString(new String(chbuff, 0, len))
      case ':' | ',' =>
        incl()
        rb.approvalSyntax(x)
        tokenize(rb)
      case '{' =>
        closingSyntaxCheckEnable = true
        incl()
        tokenize(JsStackObjects(rb))
      case '[' =>
        closingSyntaxCheckEnable = true
        incl()
        tokenize(JsStackArray(rb))
      case '}' | ']' =>
        incl()
        val skashed = rb.squash
        if (skashed.isSquashable) tokenize(skashed) else skashed
      case _ =>
        val len = detectAnyVal(0)
        if (closingSyntaxCheckEnable) {
          tokenize(rb ++ JsAnyVal.apply[String](new String(chbuff, 0, len)))
        } else JsAnyVal.apply[String](new String(chbuff, 0, len))
    }
  }
}
