package com.phylage.scaladia.json.tokenize.combinator

import com.phylage.scaladia.json.error.IllegalJsonFormat
import com.phylage.scaladia.json.tokenize.combinator.CombinationResult.TokenizeTemp
import com.phylage.scaladia.json.tokenize.{ReadStream, ResultBuff}

import scala.annotation.tailrec
import scala.language.implicitConversions

trait DirectOneCombinator extends JStreamReader with SyntaxTreeCombinator[Char, Char] {

  type Supply = ResultBuff[Char] => TokenizeTemp[Boolean]

  @tailrec
  override final def takeShiftCombine(_v: ReadStream, bf: ResultBuff[Char])(f: Char => Supply): CombinationResult[Char] = {
    val t = read(_v)
    if (t == 65535) {
      throw IllegalJsonFormat(s"EOF in an unexpected position.")
    } else if (t < 0) {
      CombinationResult(bf)
    } else f(t)(bf) match {
      case Left(e)      => CombinationResult(e)
      case Right(true)  => takeShiftCombine(_v, bf)(f)
      case Right(false) => CombinationResult(bf)
    }
  }
}
