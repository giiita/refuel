package com.phylage.scaladia.json.tokenize

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.internal.JsonCodeMap._
import com.phylage.scaladia.json.entry.{JsAnyVal, JsNull}
import com.phylage.scaladia.json.tokenize.combinator.CombinationResult.TokenizeTemp
import com.phylage.scaladia.json.tokenize.combinator.{CombinationResult, DirectOneDroplessCombinator}
import com.phylage.scaladia.json.tokenize.inject.JStringApply

private[json] object AnyValTokenizer extends JStringApply
  with DirectOneDroplessCombinator[Char]
  with JsonStreamingTokenizer[Char, Char] {

  override def combineTokenizerMap: Char => AnyValTokenizer.Supply = {
    case COMMA | COLON | OBJECT_END | ARRAY_END => _ => Supply.FALSE
    case c => bf =>
      bf.+=(c)
      Supply.TRUE
  }

  override def describe(ignorelized: CombinationResult[Char]): TokenizeTemp[Json] = {
    ignorelized.made.right.map { x =>
      x.toArray match {
        case Array('n', 'u', 'l', 'l') => JsNull
        case _ => JsAnyVal(apply(x.toArray))
      }
    }
  }
}
