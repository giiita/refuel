package refuel.json.tokenize.combinator

import refuel.json.tokenize.ReadStream

import scala.annotation.tailrec

trait ExtensibleIndexWhere {
  protected def indexWhere(arr: ReadStream, fn: Char => Boolean, from: Int = 0): Int = {
    val size = arr.length

    @tailrec
    def lp(cnt: Int): Int = {
      if (cnt == size) {
        -1
      } else if (fn(arr(cnt))) {
        cnt
      } else {
        lp(cnt + 1)
      }
    }

    lp(from)
  }
}
