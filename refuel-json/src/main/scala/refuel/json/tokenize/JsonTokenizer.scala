package refuel.json.tokenize

import refuel.json.JsonVal
import refuel.json.JsonVal.{JsAny, JsStackArray, JsStackObjects, JsString}
import refuel.json.tokenize.Types.ResultBuff
import refuel.json.tokenize.strategy.JTransformStrategy

import scala.annotation.{switch, tailrec}

abstract class JsonTokenizer(rs: Array[Char]) extends JTransformStrategy(rs) with GlowableBuffer {

  protected lazy final val Radix = 16

  protected def encodedLiteralHandle(len: Int): Int

  protected def incl(x: Int = 1): Unit = {
    pos += x
  }

  @tailrec
  protected final def tokenize(rb: ResultBuff[JsonVal]): JsonVal = {
    if (increOrFinish(_ > 32)) rb else {
      val x = rs(pos)
      (x: @switch) match {
        case '"' =>
          incl()
          val len = detectLiteral(0)
          tokenize(rb joinUnsafe JsString(new String(chbuff, 0, len)))
        case ':' | ',' =>
          incl()
          rb.approvalSyntax(x)
          tokenize(rb)
        case '{' =>
          processing = true
          incl()
          tokenize(JsStackObjects(rb))
        case '[' =>
          processing = true
          incl()
          tokenize(JsStackArray(rb))
        case '}' | ']' =>
          incl()
          val skashed = rb.squash
          if (skashed.isSquashable) tokenize(skashed) else skashed
        case _ =>
          val len = detectAnyVal(0)
            tokenize(rb joinUnsafe JsAny.apply[String](new String(chbuff, 0, len)))
      }
    }
  }

  @tailrec
  private[this] final def detectLiteral(len: Int): Int = {
    if (pos >= length) beEOF
    (rs(pos): @switch) match {
      case 0x0000 => beEOF
      case '\\' =>
        detectLiteral(encodedLiteralHandle(len))
      case '"' =>
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
}
