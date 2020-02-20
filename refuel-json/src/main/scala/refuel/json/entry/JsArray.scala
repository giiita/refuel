package refuel.json.entry

import refuel.json.Json

case class JsArray private(bf: Array[Json]) extends JsVariable {
  override def toString: String = s"""[${bf.mkString(",")}]"""

  def ++(js: Json): Json = {
    if (js == null) this else JsArray(bf :+ js)
  }

  override def prettyprint: String = s"[\n${bf.map(x => s"  $x").mkString(",\n")}\n]"
}

object JsArray {
  def apply(bf: TraversableOnce[Json]): JsArray = new JsArray(bf.toArray)
}
