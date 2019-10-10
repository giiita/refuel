package com.phylage.scaladia.json.entry

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.error.IllegalJsonSyntaxTreeBuilding

case class JsAnyVal private[entry](literal: String) extends JsVariable {
  override def toString: String = literal.toString.trim

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsAnyVal.")
}

object JsAnyVal {
  def apply(value: Any): JsAnyVal = new JsAnyVal(value.toString)
}
