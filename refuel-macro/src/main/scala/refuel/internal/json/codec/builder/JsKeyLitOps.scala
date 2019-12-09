package refuel.internal.json.codec.builder

import refuel.json.Json

trait JsKeyLitOps {
  def rec(x: Json): Seq[Json]

  def additionalKeyRef(sers: Seq[Json]): Json

  def ++(that: JsKeyLitOps): JsKeyLitOps
}