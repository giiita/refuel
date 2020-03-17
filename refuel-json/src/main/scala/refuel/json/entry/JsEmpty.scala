package refuel.json.entry

import refuel.json.JsonVal
import refuel.json.error.UnexpectedDeserializeOperation

private[refuel] case object JsEmpty extends JsVariable {

  override def toString: String = ""
  def pour(b: StringBuffer): Unit = ()

  override def ++(js: JsonVal): JsonVal = {
    if (js.isIndependent) js else throw UnexpectedDeserializeOperation(s"Cannot join $js to EmptyObject.")
  }

  override def isIndependent: Boolean = true
}
