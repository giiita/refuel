package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Json
import refuel.json.entry.JsObject

case class MultipleKeyLit(v: Seq[JsKeyLitOps]) extends JsKeyLitOps {
  override def rec(x: Json): Seq[Json] = {
    v.map(_.rec(x).reduce(_ ++ _))
  }

  def toJsonKey: Json = JsObject.dummy

  override def ++(that: JsKeyLitOps): JsKeyLitOps = copy(v :+ that)

  def additionalKeyRef(sers: Seq[Json]): Json = {
    v.zip(sers).map {
      case (lit, ser) => lit.additionalKeyRef(Seq(ser))
    }.reduce(_ ++ _)
  }
}
