package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Json
import refuel.json.codecs.builder.context.keylit.parser.KeyLitParser

case object SelfCirculationLit extends JsKeyLitOps with KeyLitParser {
  val v: Seq[String] = Nil

  override def rec(x: Json): Seq[Json] = Seq(x)

  def additionalKeyRef(sers: Seq[Json]): Json = sers.head

  def ++(that: JsKeyLitOps): JsKeyLitOps = MultipleKeyLit(Seq(this, that))

  override def prefix(that: Seq[String]): JsKeyLitOps = JsKeyLit(that)
}
