package refuel.json.tokenize

import refuel.json.Json
import refuel.json.internal.JsonCodeMap
import refuel.json.internal.JsonCodeMap._
import refuel.json.entry.JsString
import refuel.json.tokenize.combinator.CombinationResult.TokenizeTemp
import refuel.json.tokenize.combinator.{CombinationResult, MapCombinator}
import refuel.json.tokenize.inject.JStringApply

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