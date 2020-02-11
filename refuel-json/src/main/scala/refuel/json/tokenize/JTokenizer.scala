package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry._
import refuel.json.error.IllegalJsonFormat
import refuel.json.tokenize.combinator.ExtensibleIndexWhere
import refuel.json.tokenize.inject.JStringApply

import scala.annotation.{switch, tailrec}
import scala.collection.mutable.ArrayBuffer

class JTokenizer(rs: Array[Char]) extends JStringApply with ExtensibleIndexWhere {

  private[this] final val length = rs.length

  private[this] def trimmedIndex(from: Int): Int = {
    indexWhere(rs, _ > 32, from)
  }

  private[this] def SB = new StringBuilder(512)

  @tailrec
  private[this] final def foldTokenize(from: Int): Int = {
    if (from >= length) {
      throw IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
    } else {
      (rs(from): @switch) match {
        case '\\' =>
          foldTokenize(from + 2)
        case '"' =>
          from - 1
        case s =>
          SB.append(s)
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
          SB.setLength(0)
           SB.appendAll(rs.take(strEnd - ti))
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
