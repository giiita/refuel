package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.JsonVal
import refuel.json.codecs.builder.context.keylit.parser.KeyLitParser

case object SelfCirculationLit extends JsKeyLitOps with KeyLitParser {
  def rec(x: JsonVal): Seq[JsonVal] = Seq(x)

  def additionalKeyRef(sers: Seq[JsonVal]): JsonVal = sers.head

  def ++(that: JsKeyLitOps): JsKeyLitOps = MultipleKeyLit(Seq(this, that))

  def prefix(that: Seq[String]): JsKeyLitOps = JsKeyLit(that)
}
