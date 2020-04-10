package refuel.json

import refuel.json.codecs.{CodecRaiseable, Read, Write}

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/**
 * Apply / Unapply codec of T
 *
 * @tparam T Target type param.
 */
@implicitNotFound("Cannot found ${T}")
trait Codec[T] extends Read[T] with Write[T] with CodecRaiseable[T] {
  override def raise: Codec[T] = this
}

object Codec {
  private[refuel] implicit def both[T](const: (JsonVal => T, T => JsonVal)): Codec[T] = new Codec[T] {
    /**
     * Serialize Json to T format.
     * Failure should continue and propagate up.
     *
     * @param t Serializable object.
     * @return
     */
    override def serialize(t: T): JsonVal = const._2(t)

    /**
     * Deserialize Json to T format.
     * Failure should continue and propagate up.
     *
     * @param bf Json syntax tree.
     * @return
     */
    override def deserialize(bf: JsonVal): T = const._1(bf)
  }

  private[refuel] implicit def writeOnly[T](f: T => JsonVal): Write[T] = f(_)

  private[refuel] implicit def readOnly[T](f: JsonVal => T): Read[T] = f(_)
}