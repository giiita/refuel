package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry.JsObject
import refuel.json.internal.JsonCodeMap._
import refuel.json.tokenize.combinator.ExtensibleIndexWhere

import scala.annotation.{switch, tailrec}
import scala.collection.BitSet

private[json] object ObjectTokenizer
  extends ExtensibleIndexWhere
    with JsonStreamingTokenizer {

  override def run(v: ReadStream): Json = {
    val x = JsObject.dummy
    takeMap(0, v, x)
    x
    //    } match {
    //      case Success(_) => JsObject.dummy
    //      case Success(_) => throw IllegalJsonFormat(s"The conversion was successful, but the generated JsonTree is invalid.\n$v")
    //      case Failure(TokenizeFailed(msg, rest)) => throw IllegalJsonFormat(s"$msg\n${v.substring(0, v.indexOf(rest))}<ERROR FROM>$rest")
    //      case Failure(e) => throw e
    //    }
  }

  private[this] val WST = new WhiteSpaceTokenizer(BitSet(COMMA, COLON))

  override def takeMap(i: Int, rs: ReadStream, rb: ResultBuff[Json]): Int = {
    // val buff = new ArrayBuffer[Json]()

    @tailrec
    def foldTokenize(si: Int): Int = {
      if (si >= 31309) {
        return 31309
      }
      val trimmed = si// WST.takeMap(si, rs, rb)
      (rs.charAt(trimmed): @switch) match {
        case '"' =>
          foldTokenize(LiteralTokenizer.takeMap(trimmed + 1, rs, rb))
        case '[' =>
          foldTokenize(ArrayTokenizer.takeMap(trimmed + 1, rs, rb))
        case '{' =>
          foldTokenize(takeMap(trimmed + 1, rs, rb))
        case '}' =>
          trimmed + 1 // JsObject(jsons) -> (trimmed + 1) //buff
        case _ =>
          foldTokenize(AnyValTokenizer.takeMap(trimmed, rs, rb))
      }
    }

    foldTokenize(i)
  }
}
