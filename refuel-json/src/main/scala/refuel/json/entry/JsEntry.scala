package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case class JsEntry private[entry](key: Json, value: Json) extends JsVariable {
  override def toString: String = s"""${key.toString}:${value.toString}"""

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsEntry.")

  override def isIndependent: Boolean = true
}