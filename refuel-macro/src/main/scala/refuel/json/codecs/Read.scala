package refuel.json.codecs

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.{Codec, JsonVal, ReadOnly}
import refuel.json.error.DeserializeFailed

/**
 * This is a reader of json syntax tree.
 *
 * @tparam T Readable json type by this codec.
 */
trait Read[T] {
  /**
   * Deserialize Json to T format.
   * Failure should continue and propagate up.
   *
   * @param bf Json syntax tree.
   * @return
   */
  def deserialize(bf: JsonVal): Either[DeserializeFailed, T]

  def keyLiteralRef: JsKeyLitOps
}
