package refuel.json.entry

import java.util

import refuel.json.JsonVal
import refuel.json.error.IllegalJsonFormat

private[refuel] case class JsStackArray(bf: JsonVal) extends JsStack[JsonVal](bf) {
  protected var stack: Array[JsonVal] = new Array[JsonVal](1 << 2)

  private[this] var commad = 1

  private[this] def duplicateComma: Unit = {
    throw new IllegalJsonFormat(s"Duplicate comma after [${stack.filterNot(_ == null).mkString(", ")}]")
  }

  override def approvalSyntax(c: Char): Unit = {
    if (commad == 0) {
      if (c == ',')
        commad = 1
    } else duplicateComma
  }

  override def squash: JsonVal = {
    bf ++ JsArray(stack.filter(_ != null))
  }

  override def glowArray: Unit = {
    stack = util.Arrays.copyOf(stack, Integer.highestOneBit(stack.length) << 1)
  }

  def ++(js: JsonVal): JsonVal = {
    if (js != null && commad == 1) {
      if (pos == stack.length) glowArray
      stack(pos) = js
      commad = 0
      pos += 1
    }
    this
  }
}



