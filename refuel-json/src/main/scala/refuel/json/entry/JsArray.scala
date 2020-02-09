package refuel.json.entry

import refuel.json.Json

import scala.collection.mutable.ArrayBuffer

case class JsArray private[entry](bf: ArrayBuffer[Json]) extends JsVariable {
  override def toString: String = s"""[${bf.mkString(",")}]"""

  def ++(js: Json): Json = JsArray(bf :+ js)

  override def prettyprint: String = s"[\n${bf.map(x => s"  $x").mkString(",\n")}\n]"
}

object JsArray {
  def apply(bf: TraversableOnce[Json]): JsArray = new JsArray(ArrayBuffer.newBuilder[Json].++=(bf).result())
}
