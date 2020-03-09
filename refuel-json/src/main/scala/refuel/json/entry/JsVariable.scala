package refuel.json.entry

import refuel.json.Json

trait JsVariable extends Json {
  override def approvalSyntax(c: Char): Unit = ()

  override def named(key: String): Json = JsNull
}
