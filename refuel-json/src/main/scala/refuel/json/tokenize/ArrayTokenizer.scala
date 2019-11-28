package refuel.json.tokenize

import refuel.json.Json
import refuel.json.internal.JsonCodeMap._
import refuel.json.entry.JsArray
import refuel.json.tokenize.combinator.CombinationResult.TokenizeTemp
import refuel.json.tokenize.combinator.{CombinationResult, MapCombinator}

private[json] object ArrayTokenizer extends MapCombinator[Json]
  with JsonStreamingTokenizer[Json, Char] {

  private[this] val SKIP_ARRAY = WHITESPACES :+ COMMA

  override def combineTokenizerMap: Char => ArrayTokenizer.Supply = {
    case ARRAY_END                   => (_, _) => Supply.BREAK
    case x if SKIP_ARRAY.contains(x) => (_, _) => Supply.CONTINUE
    case U0022                       => (v, r) =>
      LiteralTokenizer(v).right.map { x =>
        r += x
        true
      }
    case OBJECT_START                => (v, r) =>
      ObjectTokenizer(v).right.map { x =>
        r += x
        true
      }
    case _                           => (v, r) =>
      v.reset()
      AnyValTokenizer(v).right.map { x =>
        r += x
        true
      }
  }

  override def describe(ignorelized: CombinationResult[Json]): TokenizeTemp[Json] = ignorelized.made.right.map(JsArray.apply)
}
