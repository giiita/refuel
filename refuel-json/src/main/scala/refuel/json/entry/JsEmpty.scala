package refuel.json.entry

import refuel.json.JsonVal

private[refuel] case object JsEmpty extends JsVariable {

  override def isEmpty: Boolean     = true
  override def isNonEmptry: Boolean = false

  override def toString: String     = ""
  def encode(b: StringBuffer): Unit = ()

  override def ++(js: JsonVal): JsonVal = js

  override def writeToBufferString(buffer: StringBuffer): Unit = {}
}
