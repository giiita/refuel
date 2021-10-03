package refuel.json.codecs

import refuel.json.exception.CodecDefinitionException
import refuel.json.{JsonKeySpec, JsonVal}

private[refuel] trait CodecTypeProjection[C[_]] {

  //  def readOnly[T](_read: JsonVal => T): C[T]
  def wrap[T](key: JsonKeySpec)(using codec: C[T]): C[T]
  def read[T](fn: JsonVal)(using codec: C[T]): T
  def write[T](fn: T)(using codec: C[T]): JsonVal
  def both[T](_read: JsonVal => T, _write: T => JsonVal): C[T]
}

object CodecTypeProjection {
  given CodecTypeProjection[Read] with { me =>
    def wrap[T](key: JsonKeySpec)(using codec: Read[T]): Read[T] = new Read[T] {
      def deserialize(bf: JsonVal): T = codec.deserialize(key.dig(bf))
    }

    override def read[T](fn: JsonVal)(using codec: Read[T]): T = codec.deserialize(fn)

    override def write[T](fn: T)(using codec: Read[T]): JsonVal =
      throw CodecDefinitionException(s"I am deserialization only. Cannot serialize ${fn.toString}")

    override def both[T](_read: JsonVal => T, _write: T => JsonVal): Read[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T = _read(bf)
      override def serialize(t: T): JsonVal =
        throw CodecDefinitionException(s"I am deserialization only. Cannot serialize ${t.toString}")
    }
  }

  given CodecTypeProjection[Write] with { me =>
    def wrap[T](key: JsonKeySpec)(using codec: Write[T]): Write[T] = new Write[T] {
      def serialize(t: T): JsonVal = key(codec.serialize(t))
    }

    override def read[T](fn: JsonVal)(using codec: Write[T]): T =
      throw CodecDefinitionException(s"I am serialization only. Cannot deserialize ${fn.toString}")

    override def write[T](fn: T)(using codec: Write[T]): JsonVal =
      codec.serialize(fn)

    override def both[T](_read: JsonVal => T, _write: T => JsonVal): Write[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T =
        throw CodecDefinitionException(s"I am serialization only. Cannot deserialize ${bf.toString}")
      override def serialize(t: T): JsonVal = _write(t)
    }
  }
  given CodecTypeProjection[Codec] with {
    def wrap[T](key: JsonKeySpec)(using codec: Codec[T]): Codec[T] = new Codec[T] {
      def serialize(t: T): JsonVal    = key(codec.serialize(t))
      def deserialize(bf: JsonVal): T = codec.deserialize(key.dig(bf))
    }

    override def read[T](fn: JsonVal)(using codec: Codec[T]): T = codec.deserialize(fn)

    override def write[T](fn: T)(using codec: Codec[T]): JsonVal =
      codec.serialize(fn)

    override def both[T](_read: JsonVal => T, _write: T => JsonVal): Codec[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T = _read(bf)
      override def serialize(t: T): JsonVal    = _write(t)
    }
  }
}
