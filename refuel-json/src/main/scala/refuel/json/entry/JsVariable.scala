package refuel.json.entry

import refuel.json.JsonVal

trait JsVariable extends JsonVal {
  override def approvalSyntax(c: Char): Unit = ()

  override def named(key: String): JsonVal = JsNull
}
