package com.phylage.scaladia.json.tokenize

import java.io.StringReader

import com.phylage.scaladia.injector.RecoveredInject
import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.IllegalJsonFormat
import com.phylage.scaladia.json.internal.JsonCodeMap._
import com.phylage.scaladia.json.internal.JsonTokenizer
import com.phylage.scaladia.json.entry.JsObject
import com.phylage.scaladia.json.tokenize.combinator.CombinationResult.TokenizeTemp
import com.phylage.scaladia.json.tokenize.combinator.{CombinationResult, MapCombinator}

import scala.annotation.tailrec

private[json] object ObjectTokenizer extends MapCombinator[Json]
  with JsonStreamingTokenizer[Json, Char] with RecoveredInject[JsonTokenizer] {

  @tailrec
  private[this] final def shiftValidStart(v: StringReader): Unit = {
    read(v) match {
      case OBJECT_START =>
      case x if SKIP_OBJ.contains(x) => shiftValidStart(v)
      case _ => throw IllegalJsonFormat("Json stream must be started '{'")
    }
  }

  override def run(v: String): Json = {
    val r = new StringReader(v)
    shiftValidStart(r)
    val rst = apply(r)
    r.close()
    rst match {
      case Right(x: JsObject) => x
      case Right(_) => throw IllegalJsonFormat(s"The conversion was successful, but the generated JsonTree is invalid.\n$v")
      case Left(e) if e.rest.trim.nonEmpty => throw IllegalJsonFormat(s"${e.msg}\n${v.substring(0, v.indexOf(e.rest))}<ERROR FROM>${e.rest}")
      case Left(e) => throw IllegalJsonFormat(s"${e.msg}\n$v")
    }
  }

  private[this] val SKIP_OBJ = WHITESPACES ++ Seq(COMMA, COLON)

  override def combineTokenizerMap: Char => ObjectTokenizer.Supply = {
    case x if SKIP_OBJ.contains(x) => (_, _) => Supply.CONTINUE
    case U0022 => (v, r) =>
      LiteralTokenizer(v).right.map { x =>
        r += x
        true
      }
    case OBJECT_END => (_, _) => Supply.BREAK
    case OBJECT_START => (v, r) =>
      apply(v).right.map { x =>
        r += x
        true
      }
    case ARRAY_START => (v, r) =>
      ArrayTokenizer(v).right.map { x =>
        r += x
        true
      }
    case _ => (v, r) =>
      v.reset()
      AnyValTokenizer(v).right.map { x =>
        r += x
        true
      }
  }

  override def describe(ignorelized: CombinationResult[Json]): TokenizeTemp[Json] = ignorelized.made.right.map(JsObject.apply)
}
