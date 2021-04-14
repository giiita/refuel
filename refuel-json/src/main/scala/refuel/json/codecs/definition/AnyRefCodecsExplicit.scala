package refuel.json.codecs.definition

import refuel.json.Codec
import refuel.json.codecs.{Read, Write}

import scala.reflect.ClassTag

/**
  * A codec generator that requires a type parameter.
  * AnyRefCodecs is usually used by macros to call implicit Codec generation views.
  * On the other hand, AnyRefCodecImpl is a wrapper to automatically derive recursive AnyRefCodec.
  *
  */
trait AnyRefCodecsExplicit extends AnyRefCodecs {

  /**
    * [[Seq]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def seq[T: Codec]: Codec[Seq[T]] = IterableCodec[T, Seq, Codec](implicitly[Codec[T]])

  implicit final def seqR[T: Read]: Read[Seq[T]] = IterableCodec[T, Seq, Read](implicitly[Read[T]])

  implicit final def seqW[T: Write]: Write[Seq[T]] = IterableCodec[T, Seq, Write](implicitly[Write[T]])

  /**
    * [[Set]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def set[T: Codec]: Codec[Set[T]] = SetCodec(implicitly[Codec[T]])

  implicit final def setR[T: Read]: Read[Set[T]] = SetCodec(implicitly[Read[T]])

  implicit final def setW[T: Write]: Write[Set[T]] = SetCodec(implicitly[Write[T]])

  /**
    * [[Vector]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def vector[T: Codec]: Codec[Vector[T]] = IterableCodec[T, Vector, Codec](implicitly[Codec[T]])

  implicit final def vectorR[T: Read]: Read[Vector[T]] = IterableCodec[T, Vector, Read](implicitly[Read[T]])

  implicit final def vectorW[T: Write]: Write[Vector[T]] = IterableCodec[T, Vector, Write](implicitly[Write[T]])

  /**
    * [[Array]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def array[T: Codec: ClassTag]: Codec[Array[T]] = ArrayCodec(implicitly[Codec[T]])

  implicit final def arrayR[T: Read: ClassTag]: Read[Array[T]] = ArrayCodec(implicitly[Read[T]])

  implicit final def arrayW[T: Write: ClassTag]: Write[Array[T]] = ArrayCodec(implicitly[Write[T]])

  /**
    * [[List]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def list[T: Codec]: Codec[List[T]] = IterableCodec[T, List, Codec](implicitly[Codec[T]])

  implicit final def listR[T: Read]: Read[List[T]] = IterableCodec[T, List, Read](implicitly[Read[T]])

  implicit final def listW[T: Write]: Write[List[T]] = IterableCodec[T, List, Write](implicitly[Write[T]])

  /**
    * [[Map]] codec generator.
    *
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  implicit final def map[K: Codec, V: Codec]: Codec[Map[K, V]] =
    MapCodec(implicitly[Codec[K]] -> implicitly[Codec[V]])

  implicit final def mapR[K: Read, V: Read]: Read[Map[K, V]] =
    MapCodec(implicitly[Read[K]] -> implicitly[Read[V]])

  implicit final def mapW[K: Write, V: Write]: Write[Map[K, V]] =
    MapCodec(implicitly[Write[K]] -> implicitly[Write[V]])

  /**
    * [[Option]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def option[T: Codec]: Codec[Option[T]] = OptionCodec(implicitly[Codec[T]])

  implicit final def optionR[T: Read]: Read[Option[T]] = OptionCodec(implicitly[Read[T]])

  implicit final def optionW[T: Write]: Write[Option[T]] = OptionCodec(implicitly[Write[T]])

  implicit final def either[L: Codec, R: Codec]: Codec[Either[L, R]]  = EitherCodec[L, R, Codec]
  implicit final def eitherR[L: Read, R: Read]: Read[Either[L, R]]    = EitherCodec[L, R, Read]
  implicit final def eitherW[L: Write, R: Write]: Write[Either[L, R]] = EitherCodec[L, R, Write]
}
