package refuel.json.internal

import refuel.json.JsonVal
import refuel.json.tokenize.ReadStream

/**
  * A parser combinator for decoding pure raw JSON data into a syntax tree.
  * Please note that it may currently allow some syntax errors.
  */
trait JsonTokenizer {
  def run(v: ReadStream): JsonVal
}
