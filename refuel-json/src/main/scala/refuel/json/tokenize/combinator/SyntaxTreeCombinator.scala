package refuel.json.tokenize.combinator

import refuel.json.tokenize.{ReadStream, ResultBuff}

import scala.collection.mutable.ArrayBuffer

/**
 *
 * @tparam R Combination result buffer type.
 * @tparam W Recursive stream cut splitter type.
 */
trait SyntaxTreeCombinator[R, W] {

  protected type Supply

  final def scanTakeReduce(v: ReadStream)(f: W => Supply): CombinationResult[R] = {
    takeShiftCombine(v, ArrayBuffer[R]())(f)
  }

  protected def takeShiftCombine(_v: ReadStream, bf: ResultBuff[R])(f: W => Supply): CombinationResult[R]
}
