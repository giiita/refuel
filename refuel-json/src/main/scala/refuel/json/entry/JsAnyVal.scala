package refuel.json.entry

import refuel.json.JsonVal
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case class JsAnyVal private (literal: String) extends JsVariable {
  override def pure: String = literal

  override def encode(sb: StringBuffer): Unit = sb.append(literal.trim)

  override def ++(js: JsonVal): JsonVal =
    throw IllegalJsonSyntaxTreeBuilding(s"Cannot add element $js to JsAnyVal($literal).")
}

object JsAnyVal {
  def apply[T](value: T): JsonVal = {
    if (value == null || value == "null") JsNull else new JsAnyVal(value.toString.trim)
  }
}
