package com.phylage.scaladia.json.entry

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.IllegalJsonSyntaxTreeBuilding

case object JsNull extends JsVariable {
  override def toString: String = "null"

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsNull.")
}
