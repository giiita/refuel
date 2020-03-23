package refuel.json.codecs.builder.context.translation

import refuel.json.codecs.definition.AnyRefCodecs
import refuel.json.{Codec, JsonVal}

import scala.reflect.ClassTag
import scala.util.Try

trait IterableCodecTranslator extends AnyRefCodecs {
  protected def set[T](implicit codec: Codec[T]): Codec[Set[T]] = codec

  protected def seq[T](implicit codec: Codec[T]): Codec[Seq[T]] = codec

  protected def vector[T](implicit codec: Codec[T]): Codec[Vector[T]] = codec

  protected def array[T: ClassTag](implicit codec: Codec[T]): Codec[Array[T]] = codec

  protected def option[T](implicit codec: Codec[T]): Codec[Option[T]] = new Codec[Option[T]] {
    override def deserialize(bf: JsonVal): Option[T] = Try {
      codec.deserialize(bf)
    }.toOption

    override def serialize(t: Option[T]): JsonVal = codec.serialize(t)
  }
}
