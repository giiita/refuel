package refuel.json.entry

import java.util

import refuel.json.Json
import refuel.json.error.{IllegalJsonFormat, StreamIndeterminate}

private[refuel] case class JsStackObjects(bf: Json) extends JsStack[(JsString, Json)](bf) {
  protected var stack: Array[(JsString, Json)] = new Array[(JsString, Json)](1 << 2)

  private var standBy: Option[JsString] = None

  private[this] var coloned = 0

  override def approvalSyntax(c: Char): Unit = {
    if (c == ':')
      coloned = 1
  }

  override def squash: Json = {
    if (standBy.nonEmpty || coloned == 1) illegalJsonFormat(standBy.get)
    bf ++ JsObject.fromNullableArray(stack)
  }

  private[this] final def requireAddJsStr(js: Json): Unit = {
    throw StreamIndeterminate(s"Cannot add JsKey to JsObject. Must be JsString, but was $js")
  }

  private[this] final def illegalJsonFormat(js: Json): Unit = {
    throw new IllegalJsonFormat(s"Unspecified json value of key: #$js#")
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



