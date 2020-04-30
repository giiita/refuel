package refuel.json.codecs.definition

import refuel.json.Codec

import scala.reflect.ClassTag

/**
  * A codec generator that requires a type parameter.
  * AnyRefCodecs is usually used by macros to call implicit Codec generation views.
  * On the other hand, AnyRefCodecImpl is a wrapper to automatically derive recursive AnyRefCodec.
  *
  */
trait AnyRefCodecsExport extends AnyRefCodecs {

  /**
    * [[Seq]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SeqCodecImpl[T: Codec]: Codec[Seq[T]] = SeqCodec(implicitly[Codec[T]])

  /**
    * [[Set]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def SetCodecImpl[T: Codec]: Codec[Set[T]] = SetCodec(implicitly[Codec[T]])

  /**
    * [[Vector]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def VectorCodecImpl[T: Codec]: Codec[Vector[T]] = VectorCodec(implicitly[Codec[T]])

  /**
    * [[Array]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ArrayCodecImpl[T: Codec: ClassTag]: Codec[Array[T]] = ArrayCodec(implicitly[Codec[T]])

  /**
    * [[List]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def ListCodecImpl[T: Codec]: Codec[List[T]] = ListCodec(implicitly[Codec[T]])

  /**
    * [[Map]] codec generator.
    *
    * @tparam K Inner key param type.
    * @tparam V Inner key param type.
    * @return
    */
  implicit final def MapCodecImpl[K: Codec, V: Codec]: Codec[Map[K, V]] =
    MapCodec(implicitly[Codec[K]] -> implicitly[Codec[V]])

  /**
    * [[Option]] codec generator.
    *
    * @tparam T Inner param type.
    * @return
    */
  implicit final def OptionCodecImpl[T: Codec]: Codec[Option[T]] = OptionCodec(implicitly[Codec[T]])
}
