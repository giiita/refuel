package refuel.json.tokenize

import refuel.json.tokenize.combinator.CombinationResult.TokenizeTemp
import refuel.json.tokenize.combinator.{CombinationResult, SyntaxTreeCombinator}

/**
 *
 * @tparam C Combination processing result type.
 * @tparam R Tokenizer response type.
 * @tparam W Stream take pattern type.
 */
trait Tokenizer[C, R, W] extends (ReadStream => TokenizeTemp[R]) with SyntaxTreeCombinator[C, W] {

  def run(v: String): R

  override def apply(v: ReadStream): TokenizeTemp[R]

  protected def describe(combinated: CombinationResult[C]): TokenizeTemp[R]

  protected def combineTokenizerMap: W => Supply
}