package refuel.json

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.{Read, Write}
import refuel.json.error.DeserializeFailed

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/**
 * Apply / Unapply codec of T
 *
 * @tparam T Target type param.
 */
@implicitNotFound("Cannot found ${T}")
trait Codec[T] extends Read[T] with Write[T]

object Codec {
  implicit def writeOnly[T](f: T => JsonVal): Codec[T] = new Codec[T] with WriteOnly[T] {
    /**
     * Serialize Json to T format.
     * Failure should continue and propagate up.
     *
     * @param t Serializable object.
     * @return
     */
    override def serialize(t: T): JsonVal = f(t)
  }

  implicit def readOnly[T](f: JsonVal => T): Codec[T] = new Codec[T] with ReadOnly[T] {
    /**
     * Deserialize Json to T format.
     * Failure should continue and propagate up.
     *
     * @param bf Json syntax tree.
     * @return
     */
    override def deserialize(bf: JsonVal): Either[DeserializeFailed, T] = ???

    override def keyLiteralRef: JsKeyLitOps = ???
  }
}