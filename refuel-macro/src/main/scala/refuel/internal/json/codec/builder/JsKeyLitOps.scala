package refuel.internal.json.codec.builder

import refuel.json.Json

trait JsKeyLitOps {
  def rec(x: Json): Seq[Json]

  def ++(that: JsKeyLitOps): JsKeyLitOps
}