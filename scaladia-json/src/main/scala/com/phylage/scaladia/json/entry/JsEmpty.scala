package com.phylage.scaladia.json.entry

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.{UnexpectedDeserializeOperation, UnsupportedOperation}

case object JsEmpty extends JsVariable {
  override def ++(js: Json): Json = {
    if (js.isIndependent) js else throw UnexpectedDeserializeOperation(s"Cannot join to JsEmpty. $js")
  }

  override def isIndependent: Boolean = true
}
