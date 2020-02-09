package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case class JsAnyVal private[entry](literal: String) extends JsVariable {
  override def toString: String = literal.toString.trim

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding(s"Cannot add element $js to JsAnyVal($literal).")
}

object JsAnyVal {
  def apply(value: Any): Json = value match {
    case null | "null" => JsNull
    case x => JsAnyVal(value.toString)
  }
}
