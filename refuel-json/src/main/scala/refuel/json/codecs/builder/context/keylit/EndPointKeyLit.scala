package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Json

object EndPointKeyLit extends JsKeyLitOps {
  val v: Seq[String] = Nil

  override def rec(x: Json): Seq[Json] = Seq(x)

  def additionalKeyRef(sers: Seq[Json]): Json = sers.head

  def ++(that: JsKeyLitOps): JsKeyLitOps = this

  def prefix(that: Seq[String]): JsKeyLitOps = JsKeyLit(that)
}
