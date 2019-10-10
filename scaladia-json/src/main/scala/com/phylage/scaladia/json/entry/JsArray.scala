package com.phylage.scaladia.json.entry

import com.phylage.scaladia.json.Json
import com.phylage.scaladia.json.tokenize.ResultBuff

import scala.collection.mutable.ArrayBuffer

case class JsArray private[entry](bf: ResultBuff[Json]) extends JsVariable {
  override def toString: String = s"""[${bf.mkString(",")}]"""

  def ++(js: Json): Json = JsArray(bf :+ js)
}

object JsArray {
  def apply(bf: TraversableOnce[Json]): JsArray = new JsArray(ArrayBuffer.newBuilder[Json].++=(bf).result())
}
