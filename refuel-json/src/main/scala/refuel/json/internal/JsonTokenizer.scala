package refuel.json.internal

import refuel.json.Json

/**
  * A parser combinator for decoding pure raw JSON data into a syntax tree.
  * Please note that it may currently allow some syntax errors.
  */
trait JsonTokenizer {
  def run(v: String): Json
}
