package refuel.json.tokenize.combinator

import refuel.json.error.TokenizeFailed
import refuel.json.tokenize.combinator.CombinationResult.TokenizeTemp
import refuel.json.tokenize.{ReadStream, ResultBuff}

import scala.annotation.tailrec
import scala.language.implicitConversions

trait MapCombinator[R] extends JStreamReader with SyntaxTreeCombinator[R, Char] {

  type Supply = (ReadStream, ResultBuff[R]) => TokenizeTemp[Boolean]

  protected[this] object Supply {
    val CONTINUE = Right(true)
    val BREAK = Right(false)
  }

  @tailrec
  override protected final def takeShiftCombine(_v: ReadStream, bf: ResultBuff[R])(f: Char => Supply): CombinationResult[R] = {
    _v.mark(0)
    val t = read(_v)
    if (t == 65535) {
      CombinationResult(TokenizeFailed(s"EOF in an unexpected position.", bf.mkString))
    } else if (t < 0) {
      CombinationResult(bf)
    } else {
      f(t)(_v, bf) match {
        case Left(e)      => CombinationResult(e)
        case Right(true)  => takeShiftCombine(_v, bf)(f)
        case Right(false) => CombinationResult(bf)
      }
    }
  }
}
