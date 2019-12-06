package refuel.json.codecs.builder.context.translation

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.{Codec, Json}
import refuel.json.codecs.definition.AnyRefCodecs
import refuel.json.error.DeserializeFailed

import scala.reflect.ClassTag

trait IterableCodecTranslator extends AnyRefCodecs {
  def set[T](codec: Codec[T]): Codec[Set[T]] = codec

  def seq[T](codec: Codec[T]): Codec[Seq[T]] = codec

  def vector[T](codec: Codec[T]): Codec[Vector[T]] = codec

  def array[T: ClassTag](codec: Codec[T]): Codec[Array[T]] = codec

  def option[T](codec: Codec[T]): Codec[Option[T]] = new Codec[Option[T]] {
    override def keyLiteralRef: JsKeyLitOps = codec.keyLiteralRef
    override def deserialize(bf: Json): Either[DeserializeFailed, Option[T]] = OptionCodec(codec).deserialize(keyLiteralRef.rec(bf))
    override def serialize(t: Option[T]): Json = codec.serialize(t)
  }
}
