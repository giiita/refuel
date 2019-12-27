package refuel.json.entry

import refuel.json.Json
import refuel.json.error.{UnexpectedDeserializeOperation, UnsupportedOperation}

case object JsEmpty extends JsVariable {
  override def ++(js: Json): Json = {
    if (js.isIndependent) js else throw UnexpectedDeserializeOperation(s"Cannot join $js to EmptyObject.")
  }

  override def isIndependent: Boolean = true
}
