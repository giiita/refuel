package refuel.internal.json.codec.builder

import refuel.json.JsonVal

trait JsKeyLitOps {
  def rec(x: JsonVal): Seq[JsonVal]

  def additionalKeyRef(sers: Seq[JsonVal]): JsonVal

  def ++(that: JsKeyLitOps): JsKeyLitOps

  def prefix(that: Seq[String]): JsKeyLitOps
}