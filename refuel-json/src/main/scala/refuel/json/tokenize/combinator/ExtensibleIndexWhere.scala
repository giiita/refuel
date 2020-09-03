package refuel.json.tokenize.combinator

import scala.annotation.tailrec

abstract class ExtensibleIndexWhere(rs: Array[Char]) {

  protected var pos: Int

  protected final val length = rs.length

  protected def beEOF: Int

  @tailrec
  protected final def indexWhere(fn: Char => Boolean): Unit = {
    if (pos == length) {
      beEOF
    } else if (fn(rs(pos))) {} else {
      pos += 1
      indexWhere(fn)
    }
  }
}
