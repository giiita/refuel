package refuel.json.entry

import refuel.json.Json
import refuel.json.error.StreamIndeterminate

case class JsKeyBuffer private[entry](bf: JsLiteral, jso: Json) extends JsVariable {
  override def toString: String = throw StreamIndeterminate(s"Cannot to be String. JsKey is an undefined json stream.")

  override def ++(js: Json): Json = {
    jso ++ JsEntry(bf, js)
  }
}