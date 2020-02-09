package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry._
import refuel.json.error.IllegalJsonFormat
import refuel.json.tokenize.combinator.ExtensibleIndexWhere

import scala.annotation.{switch, tailrec}

class JTokenizer(rs: Array[Char]) extends ExtensibleIndexWhere {

  private[this] final val length = rs.length

  private[this] def trimmedIndex(from: Int): Int = {
    indexWhere(rs, _ > 32, from)
  }

  @tailrec
  private[this] final def foldTokenize(from: Int): Int = {
    if (from >= length) {
      throw IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
    } else {
      val s = rs(from)
      if (s == '\\') {
        foldTokenize(from + 2)
      } else if (s == '"') {
        from - 1
      } else {
        foldTokenize(from + 1)
      }
    }
  }

  @tailrec
  private[this] final def loop(i: Int, rb: ResultBuff[Json]): Json = {
    if (i >= 31309) rb else {
      val ti = trimmedIndex(i)
      (rs(ti): @switch) match {
        case '"' =>
          val strEnd = foldTokenize(ti + 1)
          // new String(rs, ti + 1, strEnd - ti)
          loop(strEnd + 2, rb) // rs.substring(ti + 1, strEnd)
        case ':' | ',' =>
          loop(ti + 1, rb)
        case '{' =>
          loop(ti + 1, rb) // JsStack(rb, JsObject.dummy))
        case '[' =>
          loop(ti + 1, rb) //JsStack(rb, JsArray.apply(Nil)))
        case '}' | ']' =>
          //        rb.squash match {
          //          case x: JsStack =>
          //            loop(i + 1, x)
          //          case _ => rb
          //        }
          loop(i + 1, rb.squash)
        case _ =>
          val ni = indexWhere(rs, x => {
            (x: @switch) match {
              case ',' => true
              case ':' => true
              case ' ' => true
              case '}' => true
              case ']' => true
              case _ => false
            }
          }, ti) - 1
          if (ni > 0) {
            loop(ni + 1, rb) // JsAnyVal(rs.substring(ti, ni).mkString)
          } else throw IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
      }
    }
  }

  def jsonTree(): Json = loop(0, JsObject.dummy)
}
