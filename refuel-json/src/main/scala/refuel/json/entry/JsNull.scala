package refuel.json.entry

import refuel.json.JsonVal
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case object JsNull extends JsVariable {
  override final def toString: String = "null"
  def pour(b: StringBuffer): Unit     = b.append(toString)

  override def ++(js: JsonVal): JsonVal = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsNull.")
}
