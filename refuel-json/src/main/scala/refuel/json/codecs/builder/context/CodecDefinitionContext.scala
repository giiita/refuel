package refuel.json.codecs.builder.context

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.codecs.{CodecTyper, Read, Write}
import refuel.json.error.UnsupportedOperation
import refuel.json.{Codec, JsonVal}

trait CodecDefinitionContext {
  implicit case object ReadMapper extends CodecTyper[Read] { me =>
    def wrap[T](key: JsonKeyRef)(implicit codec: Read[T]): Read[T] = new Read[T] {
      def deserialize(bf: JsonVal): T = key.dig(bf).des[T]
    }

    override def read[T](fn: JsonVal)(implicit codec: Read[T]): T = codec.deserialize(fn)

    override def write[T](fn: T)(implicit codec: Read[T]): JsonVal =
      throw UnsupportedOperation(s"I am deserialization only. Cannot serialize ${fn.toString}")

    override def build[T](_read: JsonVal => T, _write: T => JsonVal): Read[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T = _read(bf)
      override def serialize(t: T): JsonVal =
        throw UnsupportedOperation(s"I am deserialization only. Cannot serialize ${t.toString}")
    }

    override def readOnly[T](_read: JsonVal => T): Read[T] =
      build(_read, { t => throw UnsupportedOperation(s"I am deserialization only. Cannot serialize ${t.toString}") })
  }
  implicit case object WriteMapper extends CodecTyper[Write] { me =>
    def wrap[T](key: JsonKeyRef)(implicit codec: Write[T]): Write[T] = new Write[T] {
      def serialize(t: T): JsonVal = key.->>(codec.serialize(t))
    }

    override def read[T](fn: JsonVal)(implicit codec: Write[T]): T =
      throw UnsupportedOperation(s"I am serialization only. Cannot deserialize ${fn.toString}")

    override def write[T](fn: T)(implicit codec: Write[T]): JsonVal =
      codec.serialize(fn)

    override def build[T](_read: JsonVal => T, _write: T => JsonVal): Write[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T =
        throw UnsupportedOperation(s"I am serialization only. Cannot deserialize ${bf.toString}")
      override def serialize(t: T): JsonVal = _write(t)
    }
    override def readOnly[T](_read: JsonVal => T): Write[T] =
      build(_read, { t => throw UnsupportedOperation(s"I am deserialization only. Cannot serialize ${t.toString}") })
  }
  implicit case object CodecMapper extends CodecTyper[Codec] {
    def wrap[T](key: JsonKeyRef)(implicit codec: Codec[T]): Codec[T] = new Codec[T] {
      def serialize(t: T): JsonVal    = key.->>(codec.serialize(t))
      def deserialize(bf: JsonVal): T = key.dig(bf).des[T]
    }

    override def read[T](fn: JsonVal)(implicit codec: Codec[T]): T = codec.deserialize(fn)

    override def write[T](fn: T)(implicit codec: Codec[T]): JsonVal =
      codec.serialize(fn)

    override def build[T](_read: JsonVal => T, _write: T => JsonVal): Codec[T] = new Codec[T] {
      override def deserialize(bf: JsonVal): T = _read(bf)
      override def serialize(t: T): JsonVal    = _write(t)
    }
    override def readOnly[T](_read: JsonVal => T): Codec[T] =
      build(_read, { t => throw UnsupportedOperation(s"I am deserialization only. Cannot serialize ${t.toString}") })
  }
}
