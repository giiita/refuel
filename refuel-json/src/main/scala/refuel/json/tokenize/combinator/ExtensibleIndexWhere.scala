package refuel.json.tokenize.combinator

import refuel.json.error.IllegalJsonFormat

import scala.annotation.tailrec

abstract class ExtensibleIndexWhere(rs: Array[Char]) {

  protected var pos: Int

  protected final val length = rs.length
  protected final val maxIndex = length - 1


  protected final def throwUnexpectedEOF: Unit = {
    throw new IllegalJsonFormat(s"Unexpected EOF: ${rs.mkString}")
  }

  @tailrec
  protected final def indexWhere(fn: Char => Boolean): Unit = {
    if (pos == length) {
      throwUnexpectedEOF
    } else if (fn(rs(pos))) {
      pos
    } else {
      pos += 1
      indexWhere(fn)
    }
  }
}
