package refuel.json.entry

import refuel.json.Json
import refuel.json.error.IllegalJsonSyntaxTreeBuilding

case class JsEntry private[entry] (key: JsString, value: Json)
    extends JsVariable {
  override def toString: String = s"""${key.toString}:${value.toString}"""

  override def prettyprint: String = s"""${key.toString}: ${value.toString}"""

  override def ++(js: Json): Json =
    throw IllegalJsonSyntaxTreeBuilding(s"Cannot add element $js to JsEntry")

  override def isIndependent: Boolean = true

  override def named(key: String): Json = {
    if (this.key.unquote == key) value else JsNull
  }
}
