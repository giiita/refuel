package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case class JsAnyVal private[entry](literal: String) extends JsVariable {
  override def toString: String = literal.toString.trim

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsAnyVal.")
}

object JsAnyVal {
  def apply(value: Any): JsAnyVal = new JsAnyVal(value.toString)
}
