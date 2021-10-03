package refuel.json.codecs

import refuel.json.exception.CodecDefinitionException
import refuel.json.{JsonKeySpec, JsonVal}

private[refuel] trait CodecTypeProjection[C[_]] {

  //  def readOnly[T](_read: JsonVal => T): C[T]
  def wrap[T](key: JsonKeySpec)(implicit codec: C[T]): C[T]
  def read[T](fn: JsonVal)(implicit codec: C[T]): T
  def write[T](fn: T)(implicit codec: C[T]): JsonVal
  def both[T](_read: JsonVal => T, _write: T => JsonVal): C[T]
}

object CodecTypeProjection {
  implicit object CodecProjection extends CodecTypeProjection[Codec] {
    def wrap[T](key: JsonKeySpec)(implicit codec: Codec[T]): Codec[T] = new Codec[T] {
      def serialize(t: T): JsonVal    = key(codec.serialize(t))
      def deserialize(bf: JsonVal): T = codec.deserialize(key.dig(bf))
    }

    override def read[T](fn: JsonVal)(implicit codec: Codec[T]): T = codec.deserialize(fn)

    override def write[T](fn: T)(implicit codec: Codec[T]): JsonVal =
      codec.serialize(fn)

    override def both[T](_read: JsonVal => T, _write: T => JsonVal): Codec[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T = _read(bf)
      override def serialize(t: T): JsonVal    = _write(t)
    }
  }
}
