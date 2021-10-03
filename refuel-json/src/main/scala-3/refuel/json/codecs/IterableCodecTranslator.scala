package refuel.json.codecs

import refuel.json.JsonTransform

import scala.reflect.ClassTag

trait IterableCodecTranslator { this: JsonTransform =>
  sealed abstract class CodecDefTransferOperator[C[_]: CodecTypeProjection] {
    final def pure[T](using codec: C[T]): C[T]   = codec
    final def set[T](using codec: C[T]): C[Set[T]]   = SetCodec[T, C].apply(codec)
    final def seq[T](using codec: C[T]): C[Seq[T]]   = SeqCodec[T, C].apply(codec)
    final def vector[T](using codec: C[T]): C[Vector[T]]   = VectorCodec[T, C].apply(codec)
    final def list[T](using codec: C[T]): C[List[T]]   = ListCodec[T, C].apply(codec)
    final def array[T: ClassTag](using codec: C[T]): C[Array[T]]   = ArrayCodec[T, C].apply(codec)
    final def option[T](using codec: C[T]): C[Option[T]]   = OptionCodec[T, C].apply(codec)
    final def map[K, V](using codec: C[(K, V)]): C[Map[K, V]] = MapCodec[K, V, C].apply(codec)
    final inline def tuple[T <: Tuple]: C[T] = tup[T, C]
  }

  /**
    * Provides functions to extend the Deserializer.
    * ```
    * class Test extends JsonTransform {
    *   val read: Read[List[String]] = ReadOf.list[String]
    * }
    * ```
    */
  object ReadOf extends CodecDefTransferOperator[Read]

  /**
    * Provides functions to extend the Serializer.
    * ```
    * class Test extends JsonTransform {
    *   val write: Write[List[String]] = WriteOf.list[String]
    * }
    * ```
    */
  object WriteOf extends CodecDefTransferOperator[Write]

  /**
    * Provides functions to extend the Codec.
    * ```
    * class Test extends JsonTransform {
    *   val codec: Codec[List[String]] = CodecOf.list[String]
    * }
    * ```
    */
  object CodecOf extends CodecDefTransferOperator[Codec]
}
