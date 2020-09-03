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

  override def writeToBufferString(buffer: StringBuffer): Unit = {
    var unempty = false
    buffer.append('[')
    bf.foreach { x =>
      if (unempty) buffer.append(',')
      x.writeToBufferString(buffer)
      if (!unempty) unempty = true
    }
    buffer.append(']')
  }
}

object JsArray {
  def apply(bf: TraversableOnce[JsonVal]): JsArray = new JsArray(bf.toSeq)
}
