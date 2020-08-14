package refuel.json.codecs.builder.context.translation

import refuel.json.Codec
import refuel.json.codecs.builder.EitherCodecConditionBuilder
import refuel.json.codecs.definition.AnyRefCodecsExplicit
import refuel.json.codecs.{Read, Write}

import scala.reflect.ClassTag

trait IterableCodecTranslator extends AnyRefCodecsExplicit {
  protected final def set[T](implicit codec: Read[T]): Read[Set[T]]   = SetCodec(codec)
  protected final def set[T](implicit codec: Write[T]): Write[Set[T]] = codec
  protected final def set[T](implicit codec: Codec[T]): Codec[Set[T]] = codec

  protected final def seq[T](implicit codec: Read[T]): Read[Seq[T]]   = codec
  protected final def seq[T](implicit codec: Write[T]): Write[Seq[T]] = codec
  protected final def seq[T](implicit codec: Codec[T]): Codec[Seq[T]] = codec

  protected final def vector[T](implicit codec: Read[T]): Read[Vector[T]]   = codec
  protected final def vector[T](implicit codec: Write[T]): Write[Vector[T]] = codec
  protected final def vector[T](implicit codec: Codec[T]): Codec[Vector[T]] = codec

  protected final def array[T: ClassTag](implicit codec: Read[T]): Read[Array[T]]   = codec
  protected final def array[T: ClassTag](implicit codec: Write[T]): Write[Array[T]] = codec
  protected final def array[T: ClassTag](implicit codec: Codec[T]): Codec[Array[T]] = codec

  protected final def option[T](implicit codec: Read[T]): Read[Option[T]]   = codec
  protected final def option[T](implicit codec: Write[T]): Write[Option[T]] = codec
  protected final def option[T](implicit codec: Codec[T]): Codec[Option[T]] = codec

  protected final def either[L, R](implicit lc: Codec[L], rc: Codec[R]): Codec[Either[L, R]] = EitherCodec
  protected final def either[L, R](implicit lc: Read[L], rc: Read[R]): Read[Either[L, R]]    = EitherCodec
  protected final def either[L, R](implicit lc: Write[L], rc: Write[R]): Write[Either[L, R]] = EitherCodec

  protected final def eitherCond[L, R](implicit lc: Codec[L], rc: Codec[R]): EitherCodecConditionBuilder[L, R, Codec] =
    new EitherCodecConditionBuilder[L, R, Codec](EitherCondCodec(_))
  protected final def eitherCond[L, R](implicit lc: Read[L], rc: Read[R]): EitherCodecConditionBuilder[L, R, Read] =
    new EitherCodecConditionBuilder[L, R, Read](EitherCondCodec(_))
  protected final def eitherCond[L, R](implicit lc: Write[L], rc: Write[R]): EitherCodecConditionBuilder[L, R, Write] =
    new EitherCodecConditionBuilder[L, R, Write](EitherCondCodec(_))
}
