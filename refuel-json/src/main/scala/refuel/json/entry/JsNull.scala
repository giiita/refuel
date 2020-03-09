package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case object JsNull extends JsVariable {
  override final def toString: String = "null"
  def pour(b: StringBuffer): Unit = b.append(toString)

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsNull.")
}
