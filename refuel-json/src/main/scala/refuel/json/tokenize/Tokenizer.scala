package refuel.json.tokenize

import refuel.json.tokenize.combinator.ExtensibleIndexWhere

/**
 * Base type of processor for building.
 * It's nothing that same as before and after reading buffer for process.
 * Therefore, always returns next reader with result value when apply this.
 *
 * @tparam R Tokenizer response type.
 */
trait Tokenizer[R, @specialized(Int, Long, Double, Char, Boolean) S] extends ExtensibleIndexWhere {
  def run(v: ReadStream): R

  def takeMap(i: S, rs: ReadStream, rb: ResultBuff[R]): S
}