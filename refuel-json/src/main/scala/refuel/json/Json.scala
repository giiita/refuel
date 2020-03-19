package refuel.json

import refuel.json.entry.JsObject

object Json {
  def obj(rows: (String, JsonVal)*): JsonVal = JsObject(rows:_*)
}
