package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case class JsAnyVal private[entry](literal: String) extends JsVariable {
  override def toString: String = literal.trim

  override def pour(sb: StringBuffer): Unit = sb.append(literal.trim)

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding(s"Cannot add element $js to JsAnyVal($literal).")
}

object JsAnyVal {
  def apply(value: Any): Json = {
    if (value == null || value == "null") JsNull else JsAnyVal(value.toString)
  }
}
