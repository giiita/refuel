package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Json

case object SelfCirculationLit extends JsKeyLitOps with KeyLitParser {
  val v: Seq[String] = Nil

  override def rec(x: Json): Seq[Json] = Seq(x)

  def additionalKeyRef(sers: Seq[Json]): Json = sers.head

  def ++(that: JsKeyLitOps): JsKeyLitOps = MultipleKeyLit(Seq(this, that))
}
