package refuel.json.entry

import refuel.json.Json

trait JsVariable extends Json {
  override def named(key: String): Json = JsNull
}
