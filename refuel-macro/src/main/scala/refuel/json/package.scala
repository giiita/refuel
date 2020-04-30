package refuel

import refuel.json.codecs.{Read, Write}
import refuel.json.error.UnsupportedOperation

package object json {
  private[refuel] trait WriteOnly[T] extends Read[T] with Write[T] {
    override final def deserialize(bf: JsonVal): T =
      throw UnsupportedOperation(s"I am serialization only. Cannot deserialize ${bf.toString}")
  }

  private[refuel] trait ReadOnly[T] extends Read[T] with Write[T] {
    override final def serialize(t: T): JsonVal =
      throw UnsupportedOperation(s"I am deserialization only. Cannot serialize $t")
  }
}
