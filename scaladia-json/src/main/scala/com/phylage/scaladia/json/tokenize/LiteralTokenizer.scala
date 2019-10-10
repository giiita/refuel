package com.phylage.scaladia.json.tokenize

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.internal.JsonCodeMap
import com.phylage.scaladia.json.internal.JsonCodeMap._
import com.phylage.scaladia.json.entry.JsString
import com.phylage.scaladia.json.tokenize.combinator.CombinationResult.TokenizeTemp
import com.phylage.scaladia.json.tokenize.combinator.{CombinationResult, MapCombinator}
import com.phylage.scaladia.json.tokenize.inject.JStringApply

private[json] object LiteralTokenizer extends JStringApply
  with JsonStreamingTokenizer[Char, Char]
  with MapCombinator[Char] {

  override def combineTokenizerMap: Char => Supply = {
    case U005C => (v, r) =>
      r.+=(U005C)

      val escaped = read(v)
      if (escaped == JsonCodeMap.UNICODE_ESCAPE) {
        val unicode = apply(
          (1 to 4).map { i =>
            read(v)
          }.toArray
        )
        r.+=(Integer.parseInt(unicode, 16).toChar)
      } else {
        r.+=(escaped)
      }
      Supply.CONTINUE
    case U0022 => (_, _) => Supply.BREAK
    case x     => (_, r) =>
      r.+=(x)
      Supply.CONTINUE
  }

  override def describe(ignorelized: CombinationResult[Char]): TokenizeTemp[Json] = ignorelized.made.right.map { x =>
    JsString {
      apply(
        x.toArray
      )
    }
  }
}