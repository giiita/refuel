package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry.{JsNull, JsObject}
import refuel.json.error.{IllegalJsonFormat, TokenizeFailed}
import refuel.json.tokenize.combinator.ExtensibleIndexWhere
import refuel.json.tokenize.inject.JStringApply

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

private[json] object LiteralTokenizer extends JStringApply
  with JsonStreamingTokenizer
  with ExtensibleIndexWhere {

  override def run(v: ReadStream): Json = {
    Try {
      takeMap(0, v, JsObject.dummy)
    } match {
      case Success(_) => JsNull
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

    @tailrec
    def foldTokenize(from: Int): Int = {
      val ni = indexWhere(rs, _ == '"', from)
      if (ni > 0) {
        if (rs.charAt(ni - 1) == '\\') {
          foldTokenize(ni + 1)
        } else {
          rb.++(JsNull)
          ni + 1 // JsString(chars.mkString) -> (ni + 1) //stack(ni - i)
        }
      } else throw IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
    }

    foldTokenize(i)
    //      case (U005C, v, r) =>
    //        r.+=(U005C)
    //
    //        val escaped = read(v)
    //        if (escaped == JsonCodeMap.UNICODE_ESCAPE) {
    //          val unicode = apply(
    //            (1 to 4).map { _ =>
    //              read(v)
    //            }.toArray
    //          )
    //          r.+=(Integer.parseInt(unicode, 16).toChar)
    //        } else {
    //          r.+=(escaped)
    //        }
  }
}