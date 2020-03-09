package refuel.json.entry

import refuel.json.Json
import refuel.json.error.UnexpectedDeserializeOperation

private[refuel] case object JsEmpty extends JsVariable {

  override def toString: String = ""
  def pour(b: StringBuffer): Unit = ()

  override def ++(js: Json): Json = {
    if (js.isIndependent) js else throw UnexpectedDeserializeOperation(s"Cannot join $js to EmptyObject.")
  }

  override def isIndependent: Boolean = true
}
