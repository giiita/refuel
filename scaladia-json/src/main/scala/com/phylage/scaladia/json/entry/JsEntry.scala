package com.phylage.scaladia.json.entry

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.IllegalJsonSyntaxTreeBuilding

case class JsEntry private[entry](key: Json, value: Json) extends JsVariable {
  override def toString: String = s"""${key.toString}:${value.toString}"""

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsEntry.")

  override def isIndependent: Boolean = true
}