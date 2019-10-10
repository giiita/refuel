package com.phylage.scaladia.json.tokenize.combinator

import com.phylage.scaladia.json.error.IllegalJsonFormat
import com.phylage.scaladia.json.tokenize.combinator.CombinationResult.TokenizeTemp
import com.phylage.scaladia.json.tokenize.{ReadStream, ResultBuff}

import scala.annotation.tailrec
import scala.language.implicitConversions

trait DirectOneDroplessCombinator[R] extends JStreamReader with SyntaxTreeCombinator[R, Char] {

  override type Supply = ResultBuff[R] => TokenizeTemp[Boolean]

  protected[this] object Supply {
    val TRUE = Right(true)
    val FALSE = Right(false)
  }

  @tailrec
  protected final def takeShiftCombine(_v: ReadStream, bf: ResultBuff[R])(f: Char => Supply): CombinationResult[R] = {
    _v.mark(0)
    val t = read(_v)
    if (t == 65535) {
      throw IllegalJsonFormat(s"EOF in an unexpected position.")
    } else if (t < 0) {
      CombinationResult(bf)
    } else f(t)(bf) match {
      case Left(e)      => CombinationResult(e)
      case Right(true)  => takeShiftCombine(_v, bf)(f)
      case Right(false) => {
        _v.reset()
        CombinationResult(bf)
      }
    }
  }
}
