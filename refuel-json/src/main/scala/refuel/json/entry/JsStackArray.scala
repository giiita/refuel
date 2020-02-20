package refuel.json.entry

import java.util

import refuel.json.Json

case class JsStackArray(bf: Json) extends JsStack[Json](bf) {
  protected var stack: Array[Json] = new Array[Json](1 << 2)

  override def squash: Json = {
    bf ++ JsArray(stack.filter(_ != null))
  }

  override def glowArray: Unit = {
    stack = util.Arrays.copyOf(stack, Integer.highestOneBit(stack.length) << 1)
  }

  def ++(js: Json): Json = {
    if (js != null) {
      if (pos == stack.length) glowArray
      stack(pos) = js
      pos += 1
    }
    this
  }
}



