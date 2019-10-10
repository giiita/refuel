package com.phylage.scaladia.json.tokenize

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.internal.JsonTokenizer
import com.phylage.scaladia.json.tokenize.combinator.CombinationResult.TokenizeTemp

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
