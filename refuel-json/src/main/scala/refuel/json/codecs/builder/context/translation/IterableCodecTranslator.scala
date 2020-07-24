package refuel.json.codecs.builder.context.translation

import refuel.json.Codec
import refuel.json.codecs.definition.AnyRefCodecs
import refuel.json.codecs.{Read, Write}

import scala.reflect.ClassTag

trait IterableCodecTranslator extends AnyRefCodecs {
  protected def set[T](implicit codec: Read[T]): Read[Set[T]]   = SetCodec(codec)
  protected def set[T](implicit codec: Write[T]): Write[Set[T]] = codec
  protected def set[T](implicit codec: Codec[T]): Codec[Set[T]] = codec

  protected def seq[T](implicit codec: Read[T]): Read[Seq[T]]   = codec
  protected def seq[T](implicit codec: Write[T]): Write[Seq[T]] = codec
  protected def seq[T](implicit codec: Codec[T]): Codec[Seq[T]] = codec

  protected def vector[T](implicit codec: Read[T]): Read[Vector[T]]   = codec
  protected def vector[T](implicit codec: Write[T]): Write[Vector[T]] = codec
  protected def vector[T](implicit codec: Codec[T]): Codec[Vector[T]] = codec

  protected def array[T: ClassTag](implicit codec: Read[T]): Read[Array[T]]   = codec
  protected def array[T: ClassTag](implicit codec: Write[T]): Write[Array[T]] = codec
  protected def array[T: ClassTag](implicit codec: Codec[T]): Codec[Array[T]] = codec

  protected def option[T](implicit codec: Read[T]): Read[Option[T]]   = codec
  protected def option[T](implicit codec: Write[T]): Write[Option[T]] = codec
  protected def option[T](implicit codec: Codec[T]): Codec[Option[T]] = codec
}
