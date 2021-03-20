package refuel.json

import refuel.json.entry._

object Json {
  def obj(rows: (String, JsonVal)*): JsonVal = JsObject(rows: _*)
  def arr(rows: JsonVal*): JsonVal           = JsArray(rows)
  def any(row: Any): JsonVal                 = JsAnyVal(row.toString)
  def str(row: String): JsonVal              = JsString(row)
  def Null: JsonVal                          = JsNull
  def Empty: JsonVal                         = JsEmpty
}
