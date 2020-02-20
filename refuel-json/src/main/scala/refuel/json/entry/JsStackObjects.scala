package refuel.json.entry

import java.util

import refuel.json.Json
import refuel.json.error.StreamIndeterminate

case class JsStackObjects(bf: Json) extends JsStack[(JsString, Json)](bf) {
  protected var stack: Array[(JsString, Json)] = new Array[(JsString, Json)](1 << 2)

  private var standBy: Option[JsString] = None

  override def squash: Json = {
    bf ++ new JsObject(stack)
  }

  private[this] final def requireAddJsStr(js: Json): Unit = {
    throw StreamIndeterminate(s"Cannot add JsKey to JsObject. Must be JsString, but was $js")
  }

  override def glowArray: Unit = {
    stack = util.Arrays.copyOf(stack, Integer.highestOneBit(stack.length) << 1)
  }

  def ++(js: Json): Json = {
    if (js != null) {
      if (standBy.isEmpty) {
        js match {
          case x: JsString =>
            standBy = Some(x)
          case _ => requireAddJsStr(js)
        }
      } else {
        if (pos == stack.length) glowArray
        stack(pos) = (standBy.get, js)
        standBy = None
        pos += 1
      }
    }
    this
  }
}



