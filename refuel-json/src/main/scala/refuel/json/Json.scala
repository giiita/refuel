package refuel.json

import refuel.json.entry.{JsArray, JsNull, JsObject, JsString}

object Json {
  def obj(rows: (String, JsonVal)*): JsonVal = JsObject(rows: _*)
  def arr(rows: JsonVal*): JsonVal           = JsArray(rows)
  def str(row: String): JsonVal              = JsString(row)
  def Null: JsonVal                          = JsNull
}
