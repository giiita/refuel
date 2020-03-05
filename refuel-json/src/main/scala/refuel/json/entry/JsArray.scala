package refuel.json.entry

import refuel.json.Json

case class JsArray private(bf: Seq[Json]) extends JsVariable {
  override def toString: String = {
    val x = new StringBuffer()
    pour(x)
    x.toString
  }

  override def pour(sb: StringBuffer): Unit = {
    var unempty = false
    sb.append('[')
    bf.foreach { x =>
      if (unempty) sb.append(',')
      x.pour(sb)
      if (!unempty) unempty = true
    }
    sb.append(']')
  }

  def ++(js: Json): Json = {
    if (js == null) this else JsArray(bf :+ js)
  }

  override def prettyprint: String = s"[\n${bf.map(x => s"  $x").mkString(",\n")}\n]"
}

object JsArray {
  def apply(bf: TraversableOnce[Json]): JsArray = new JsArray(bf.toSeq)
}
