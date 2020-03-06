package refuel.json.tokenize.combinator

import refuel.json.error.TokenizeFailed
import refuel.json.tokenize.ResultBuff

class CombinationResult[R](val made: Either[TokenizeFailed, ResultBuff[R]]) {
  def ++(x: CombinationResult[R]): CombinationResult[R] = {
    for {
      ex <- this.made.right
      in <- x.made.right
    } yield ex.++=(in)
    this
  }

  def +(x: R): CombinationResult[R] = {
    this.made.right.foreach(_.+=(x))
    this
  }
}

object CombinationResult {
  def apply[R](v: ResultBuff[R]): CombinationResult[R] = new CombinationResult(Right(v))

  def apply[R](v: TokenizeFailed): CombinationResult[R] = new CombinationResult(Left(v))

  type TokenizeTemp[R] = Either[TokenizeFailed, R]
}
