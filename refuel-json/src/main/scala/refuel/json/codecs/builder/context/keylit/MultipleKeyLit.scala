package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Json

case class MultipleKeyLit(v: Seq[JsKeyLitOps]) extends JsKeyLitOps {
  override def rec(x: Json): Seq[Json] = {
    v.map(_.rec(x).reduce(_ ++ _))
  }

  override def ++(that: JsKeyLitOps): JsKeyLitOps = copy(v :+ that)
}
