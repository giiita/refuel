package refuel.json.tokenize

import refuel.json.Json
import refuel.json.internal.JsonTokenizer
import refuel.json.tokenize.combinator.CombinationResult.TokenizeTemp

/**
 *
 * @tparam R Combine result type.
 */
trait JsonStreamingTokenizer[R, W] extends Tokenizer[R, Json, W] with JsonTokenizer {

  override def run(v: String): Json = throw new UnsupportedOperationException(s"Cannot start tokenize at $this ")

  def apply(v: ReadStream): TokenizeTemp[Json] = {
    describe(scanTakeReduce(v)(combineTokenizerMap))
  }
}
