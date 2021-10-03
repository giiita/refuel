package refuel.json

import refuel.json.JsonVal._

object Json {
  def obj(rows: (String, JsonVal)*): JsonVal = JsObject(rows: _*)
  def arr(rows: JsonVal*): JsonVal           = JsArray(rows)
  def any(row: Any): JsonVal                 = JsAny(row.toString)
  def str(row: String): JsString              = JsString(row)
  def Null: JsonVal                          = JsNull
  def Empty: JsonVal                         = JsEmpty
}
