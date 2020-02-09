package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry.{JsNull, JsObject}
import refuel.json.error.{IllegalJsonFormat, TokenizeFailed}
import refuel.json.tokenize.combinator.ExtensibleIndexWhere
import refuel.json.tokenize.inject.JStringApply

import scala.annotation.switch
import scala.util.{Failure, Success, Try}

private[json] object AnyValTokenizer extends JStringApply
  with ExtensibleIndexWhere
  with JsonStreamingTokenizer {

  override def run(v: ReadStream): Json = {
    Try {
      takeMap(0, v, JsObject.dummy)

    } match {
      case Success(_) => JsObject.dummy
      case Success(_) => throw IllegalJsonFormat(s"The conversion was successful, but the generated JsonTree is invalid.\n$v")
      case Failure(TokenizeFailed(msg, rest)) => throw IllegalJsonFormat(s"$msg\n${v.take(v.indexOf(rest))}<ERROR FROM>$rest")
      case Failure(e) => throw e
    }
  }

  override def takeMap(i: Int, rs: ReadStream, rb: ResultBuff[Json]): Int = {

    //    def stack(n: Int): ResultBuff[Char] = {
    //      val buff = new ResultBuff[Char](n)
    //      (0 until n).foreach(si => buff.+=(rs.charAt(i + si)))
    //      buff
    //    }

    val ni = indexWhere(rs, x => {
      (x: @switch) match {
        case ',' => true
        case ':' => true
        case ' ' => true
        case '}' => true
        case ']' => true
        case _ => false
      }
    }, i)
    if (ni == 4 && rs.take(4).mkString == "null") {
      rb ++ JsNull
      ni + 1
    } else if (ni > 0) {
      rb ++ JsNull
      ni + 1 // ResultBuff()stack(ni - i)
    } else throw IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
  }
}
