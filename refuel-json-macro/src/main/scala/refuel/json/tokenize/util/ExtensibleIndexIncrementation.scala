package refuel.json.tokenize.util

import refuel.json.exception.IllegalJsonFormatException

import scala.annotation.tailrec

abstract class ExtensibleIndexIncrementation(rs: Array[Char]) {

  protected final val length = {
    val len = rs.length
    len
  }
  protected var pos: Int = 0
  protected var processing: Boolean = false
  protected def beEOF[T]: T = {
    throw IllegalJsonFormatException(s"Unexpected EOF: ${rs.mkString}")
  }

  @tailrec
  protected final def increOrFinish(fn: Char => Boolean): Boolean = {
    if (pos == length) {
      if (processing) beEOF else true
    } else if (fn(rs(pos))) false else {
      pos += 1
      increOrFinish(fn)
    }
  }
}
