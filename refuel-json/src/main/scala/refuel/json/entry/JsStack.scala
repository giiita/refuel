package refuel.json.entry

import refuel.json.Json
import refuel.json.error.StreamIndeterminate

case class JsStack private[entry](bf: Json, stack: Json) extends JsVariable {

  override def toString: String = throw StreamIndeterminate(s"Cannot to be String. JsStack is an unclosed json stream.")

  override def prettyprint: String = throw StreamIndeterminate(s"Cannot to be String. JsStack is an unclosed json stream.")

  def ++(js: Json): Json = {
    copy(stack = stack.++(js))
  }
  override def squash = bf.++(stack)
}



