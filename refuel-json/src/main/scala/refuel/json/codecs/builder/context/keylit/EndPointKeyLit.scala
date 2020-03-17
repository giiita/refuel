package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.JsonVal

object EndPointKeyLit extends JsKeyLitOps {
  def rec(x: JsonVal): Seq[JsonVal] = Seq(x)

  def additionalKeyRef(sers: Seq[JsonVal]): JsonVal = sers.head

  def ++(that: JsKeyLitOps): JsKeyLitOps = this

  def prefix(that: Seq[String]): JsKeyLitOps = JsKeyLit(that)
}
