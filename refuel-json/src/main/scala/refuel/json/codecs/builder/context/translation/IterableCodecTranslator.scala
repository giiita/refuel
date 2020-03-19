package refuel.json.codecs.builder.context.translation

import refuel.json.codecs.definition.AnyRefCodecs
import refuel.json.{Codec, JsonVal}

import scala.reflect.ClassTag
import scala.util.Try

trait IterableCodecTranslator extends AnyRefCodecs {
  def set[T](codec: Codec[T]): Codec[Set[T]] = codec

  def seq[T](codec: Codec[T]): Codec[Seq[T]] = codec

  def vector[T](codec: Codec[T]): Codec[Vector[T]] = codec

  def array[T: ClassTag](codec: Codec[T]): Codec[Array[T]] = codec

  def option[T](codec: Codec[T]): Codec[Option[T]] = new Codec[Option[T]] {
    override def deserialize(bf: JsonVal): Option[T] = Try {
      codec.deserialize(bf)
    }.toOption

    override def serialize(t: Option[T]): JsonVal = codec.serialize(t)
  }
}
