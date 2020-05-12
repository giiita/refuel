package refuel.json

import refuel.json.entry.{JsArray, JsObject}

object Json {
  def obj(rows: (String, JsonVal)*): JsonVal = JsObject(rows: _*)
  def arr(rows: JsonVal*): JsonVal           = JsArray(rows)
}
