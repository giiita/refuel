package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry.{JsArray, JsNull, JsObject}
import refuel.json.error.{IllegalJsonFormat, TokenizeFailed}
import refuel.json.internal.JsonCodeMap._
import refuel.json.tokenize.combinator.ExtensibleIndexWhere

import scala.annotation.{switch, tailrec}
import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}

private[json] object ArrayTokenizer extends ExtensibleIndexWhere
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

  private[this] val WST = new WhiteSpaceTokenizer(BitSet(COMMA))

  override final def takeMap(i: Int, rs: ReadStream, rb: ResultBuff[Json]): Int = {
    // val buff = new ArrayBuffer[Json]()

    @tailrec
    def foldTokenize(si: Int): Int = {
      val trimmed = si // WST.takeMap(si, rs, rb)
      (rs.charAt(trimmed): @switch) match {
        case '"' =>
          foldTokenize(LiteralTokenizer.takeMap(trimmed + 1, rs, rb))
        case '{' =>
          foldTokenize(ObjectTokenizer.takeMap(trimmed + 1, rs, rb))
        case ']' =>
          trimmed + 1 // JsArray(jsons) -> (trimmed + 1)
        case _ =>
          foldTokenize(AnyValTokenizer.takeMap(trimmed, rs, rb))
      }
    }

    foldTokenize(i)
  }
}
