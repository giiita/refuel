package refuel.json.codecs.definition

import refuel.json.Codec
import refuel.json.codecs.builder.EitherCodecConditionBuilder
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
  implicit final def SeqCodecImpl[T: Codec]: Codec[Seq[T]] = SeqCodec(implicitly[Codec[T]])

  implicit final def SeqReadImpl[T: Read]: Read[Seq[T]] = SeqCodec(implicitly[Read[T]])

  implicit final def SeqWriteImpl[T: Write]: Write[Seq[T]] = SeqCodec(implicitly[Write[T]])

  /**
    * [[Set]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SetCodecImpl[T: Codec]: Codec[Set[T]] = SetCodec(implicitly[Codec[T]])

  implicit final def SetReadImpl[T: Read]: Read[Set[T]] = SetCodec(implicitly[Read[T]])

  implicit final def SetWriteImpl[T: Write]: Write[Set[T]] = SetCodec(implicitly[Write[T]])

  /**
    * [[Vector]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def VectorCodecImpl[T: Codec]: Codec[Vector[T]] = VectorCodec(implicitly[Codec[T]])

  implicit final def VectorReadImpl[T: Read]: Read[Vector[T]] = VectorCodec(implicitly[Read[T]])

  implicit final def VectorWriteImpl[T: Write]: Write[Vector[T]] = VectorCodec(implicitly[Write[T]])

  /**
    * [[Array]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ArrayCodecImpl[T: Codec: ClassTag]: Codec[Array[T]] = ArrayCodec(implicitly[Codec[T]])

  implicit final def ArrayReadImpl[T: Read: ClassTag]: Read[Array[T]] = ArrayCodec(implicitly[Read[T]])

  implicit final def ArrayWriteImpl[T: Write: ClassTag]: Write[Array[T]] = ArrayCodec(implicitly[Write[T]])

  /**
    * [[List]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ListCodecImpl[T: Codec]: Codec[List[T]] = ListCodec(implicitly[Codec[T]])

  implicit final def ListReadImpl[T: Read]: Read[List[T]] = ListCodec(implicitly[Read[T]])

  implicit final def ListWriteImpl[T: Write]: Write[List[T]] = ListCodec(implicitly[Write[T]])

  /**
    * [[Map]] codec generator.
    *
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  implicit final def MapCodecImpl[K: Codec, V: Codec]: Codec[Map[K, V]] =
    MapCodec(implicitly[Codec[K]] -> implicitly[Codec[V]])

  implicit final def MapCodecImpl[K: Read, V: Read]: Read[Map[K, V]] =
    MapCodec(implicitly[Read[K]] -> implicitly[Read[V]])

  implicit final def MapCodecImpl[K: Write, V: Write]: Write[Map[K, V]] =
    MapCodec(implicitly[Write[K]] -> implicitly[Write[V]])

  /**
    * [[Option]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def OptionCodecImpl[T: Codec]: Codec[Option[T]] = OptionCodec(implicitly[Codec[T]])

  implicit final def OptionCodecImpl[T: Read]: Read[Option[T]] = OptionCodec(implicitly[Read[T]])

  implicit final def OptionCodecImpl[T: Write]: Write[Option[T]] = OptionCodec(implicitly[Write[T]])
}
