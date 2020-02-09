package refuel.json.tokenize

import refuel.json.Json
import refuel.json.internal.JsonTokenizer

/**
 * Base type of processor for json syntax tree building.
 * It's nothing that same as before and after reading buffer for process.
 * Therefore, always returns next reader with result value when apply this.
 */
trait JsonStreamingTokenizer extends Tokenizer[Json, Int] with JsonTokenizer {
}
