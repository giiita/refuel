package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case object JsNull extends JsVariable {
  override def toString: String = "null"

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsNull.")
}
