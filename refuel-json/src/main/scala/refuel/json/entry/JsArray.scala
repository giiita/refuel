package refuel.json.entry

import refuel.json.JsonVal

case class JsArray private (bf: Seq[JsonVal]) extends JsVariable {
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

  def ++(js: JsonVal): JsonVal = {
    if (js == null) this
    else {
      js match {
        case JsArray(x) => JsArray(bf ++ x)
        case _          => JsArray(bf :+ js)
      }
    }
  }

  override def named(key: String): JsonVal = copy(bf.map(_.named(key)))

  override def prettyprint: String = s"[\n${bf.map(x => s"  $x").mkString(",\n")}\n]"
}

object JsArray {
  def apply(bf: TraversableOnce[JsonVal]): JsArray = new JsArray(bf.toSeq)
}
