package refuel.json.entry

import java.util

import refuel.json.JsonVal
import refuel.json.error.{IllegalJsonFormat, StreamIndeterminate}

private[refuel] case class JsStackObjects(bf: JsonVal) extends JsStack[(JsString, JsonVal)](bf) {
  protected var stack: Array[(JsString, JsonVal)] = new Array[(JsString, JsonVal)](1 << 2)

  private var standBy: Option[JsString] = None

  private[this] var coloned = 0

  override def approvalSyntax(c: Char): Unit = {
    if (c == ':')
      coloned = 1
  }

  override def squash: JsonVal = {
    if (standBy.nonEmpty || coloned == 1) illegalJsonFormat(standBy.get)
    bf ++ JsObject.fromNullableArray(stack)
  }

  private[this] final def requireAddJsStr(js: JsonVal): Unit = {
    throw StreamIndeterminate(s"Cannot add JsKey to JsObject. Must be JsString, but was $js")
  }

  private[this] final def illegalJsonFormat(js: JsonVal): Unit = {
    throw new IllegalJsonFormat(s"Unspecified json value of key: #$js#")
  }

  override def glowArray: Unit = {
    stack = util.Arrays.copyOf(stack, Integer.highestOneBit(stack.length) << 1)
  }

  def ++(js: JsonVal): JsonVal = {
    if (js != null) {
      if (standBy.isEmpty) {
        js match {
          case x: JsString =>
            standBy = Some(x)
          case _ => requireAddJsStr(js)
        }
      } else if (coloned == 1) {
        if (pos == stack.length) glowArray
        stack(pos) = (standBy.get, js)
        standBy = None
        coloned = 0
        pos += 1
      } else {
        illegalJsonFormat(standBy.get)
      }
    }
    this
  }
}



