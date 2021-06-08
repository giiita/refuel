package refuel.json.codecs

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.JsonVal

private[refuel] trait CodecTyper[C[_]] {
  def wrap[T](key: JsonKeyRef)(implicit codec: C[T]): C[T]

  def read[T](fn: JsonVal)(implicit codec: C[T]): T
  def write[T](fn: T)(implicit codec: C[T]): JsonVal

  def readOnly[T](_read: JsonVal => T): C[T]
  def build[T](_read: JsonVal => T, _write: T => JsonVal): C[T]
}