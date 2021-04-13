package refuel.json.entry

import refuel.json.JsonVal

case class JsArray private (bf: Seq[JsonVal]) extends JsVariable {
  override def encode(sb: StringBuffer): Unit = {
    var unempty = false
    sb.append('[')
    bf.foreach { x =>
      if (unempty) sb.append(',')
      x.encode(sb)
      if (!unempty) unempty = true
    }
    sb.append(']')
  }

  override def named(key: String): JsonVal = {
    bf.map(_.named(key)).foldLeft(JsArray(None))(_ ++ _)
  }

  def ++(js: JsonVal): JsArray = {
    if (js == null) this
    else {
      js match {
        case JsArray(x) => JsArray(bf ++ x)
        case JsNull     => this
        case _          => JsArray(bf :+ js)
      }
    }
  }
}

object JsArray {
  def apply(bf: TraversableOnce[JsonVal]): JsArray = new JsArray(bf.toSeq)
}
