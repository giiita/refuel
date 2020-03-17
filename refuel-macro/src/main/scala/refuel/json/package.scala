package refuel
import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.{Read, Write}
import refuel.json.error.{DeserializeFailed, UnsupportedOperation}

package object json {
  private[refuel] trait WriteOnly[T] extends Read[T] with Write[T] {
    override final def deserialize(bf: JsonVal): Either[DeserializeFailed, T] = throw UnsupportedOperation(s"I am serialization only. Cannot deserialize ${bf.toString}")
    override final def keyLiteralRef: JsKeyLitOps = throw UnsupportedOperation(s"I am serialization only.")
  }

  private[refuel] trait ReadOnly[T] extends Read[T] with Write[T] {
    override final def serialize(t: T): JsonVal = throw UnsupportedOperation(s"I am deserialization only. Cannot serialize $t")
  }
}
