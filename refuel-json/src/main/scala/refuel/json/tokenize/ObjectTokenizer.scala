package refuel.json.tokenize

import java.io.StringReader

import refuel.json.Json
import refuel.json.entry.JsObject
import refuel.json.error.IllegalJsonFormat
import refuel.json.internal.JsonCodeMap._
import refuel.json.tokenize.combinator.CombinationResult.TokenizeTemp
import refuel.json.tokenize.combinator.{CombinationResult, MapCombinator}

import scala.annotation.tailrec

private[json] object ObjectTokenizer
  extends MapCombinator[Json]
    with JsonStreamingTokenizer[Json, Char] {

  @tailrec
  private[this] final def shiftValidStart(v: StringReader): Unit = {
    read(v) match {
      case OBJECT_START =>
      case x if SKIP_OBJ.contains(x) => shiftValidStart(v)
      case x => throw IllegalJsonFormat(s"Json stream must be started '{' but was '$x'")
    }
  }

  override def run(v: String): Json = {
    val r = new StringReader(v)
    shiftValidStart(r)
    val rst = apply(r)
    r.close()
    rst match {
      case Right(x: JsObject) => x
      case Right(_) =>
        throw IllegalJsonFormat(
          s"The conversion was successful, but the generated JsonTree is invalid.\n$v"
        )
      case Left(e) if e.rest.trim.nonEmpty && v.indexOf(e.rest) >= 0 =>
        throw IllegalJsonFormat(
          s"${e.msg}\n${v.substring(0, v.indexOf(e.rest))}<ERROR FROM>${e.rest}"
        )
      case Left(e) => throw IllegalJsonFormat(s"${e.msg}\n$v")
    }
  }

  private[this] val SKIP_OBJ = WHITESPACES ++ Seq(COMMA, COLON)

  override def combineTokenizerMap: Char => ObjectTokenizer.Supply = {
    case x if SKIP_OBJ.contains(x) =>
      (_, _) =>
        Supply.CONTINUE
    case U0022 =>
      (v, r) =>
        LiteralTokenizer(v).right.map { x =>
          r += x
          true
        }
    case OBJECT_END =>
      (_, _) => Supply.BREAK
    case OBJECT_START =>
      (v, r) =>
        apply(v).right.map { x =>
          r += x
          true
        }
    case ARRAY_START =>
      (v, r) =>
        ArrayTokenizer(v).right.map { x =>
          r += x
          true
        }
    case _ =>
      (v, r) =>
        v.reset()
        AnyValTokenizer(v).right.map { x =>
          r += x
          true
        }
  }

  override def describe(
                         ignorelized: CombinationResult[Json]
                       ): TokenizeTemp[Json] = ignorelized.made.right.map(JsObject.apply)
}
