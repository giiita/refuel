package refuel.json.codecs.builder.context.translation
import refuel.json.Codec
import refuel.json.codecs.definition.AnyRefCodecs

import scala.reflect.ClassTag

trait IterableCodecTranslator extends AnyRefCodecs {
  def set[T](codec: Codec[T]): Codec[Set[T]] = codec
  def seq[T](codec: Codec[T]): Codec[Seq[T]] = codec
  def vector[T](codec: Codec[T]): Codec[Vector[T]] = codec
  def array[T: ClassTag](codec: Codec[T]): Codec[Array[T]] = codec
  def option[T](codec: Codec[T]): Codec[Option[T]] = codec
}
