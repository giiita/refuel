package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.JsonVal

case class MultipleKeyLit(v: Seq[JsKeyLitOps]) extends JsKeyLitOps {
  override def rec(x: JsonVal): Seq[JsonVal] = {
    v.map(_.rec(x).reduce(_ ++ _))
  }

  override def ++(that: JsKeyLitOps): JsKeyLitOps = copy(v :+ that)

  def additionalKeyRef(sers: Seq[JsonVal]): JsonVal = {
    v.zip(sers).map {
      case (lit, ser) => lit.additionalKeyRef(Seq(ser))
    }.reduce(_ ++ _)
  }

  override def prefix(that: Seq[String]): JsKeyLitOps = copy(v.map(_.prefix(that)))
}
