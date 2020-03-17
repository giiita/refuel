package refuel.json.codecs.builder.context.translation

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.{Codec, JsonVal}
import refuel.json.codecs.definition.AnyRefCodecs
import refuel.json.entry.JsNull
import refuel.json.error.DeserializeFailed

import scala.reflect.ClassTag

trait IterableCodecTranslator extends AnyRefCodecs {
  def set[T](codec: Codec[T]): Codec[Set[T]] = codec

  def seq[T](codec: Codec[T]): Codec[Seq[T]] = codec

  def vector[T](codec: Codec[T]): Codec[Vector[T]] = codec

  def array[T: ClassTag](codec: Codec[T]): Codec[Array[T]] = codec

  def option[T](codec: Codec[T]): Codec[Option[T]] = new Codec[Option[T]] {
    override def keyLiteralRef: JsKeyLitOps = codec.keyLiteralRef
    override def deserialize(bf: JsonVal): Either[DeserializeFailed, Option[T]] =
      if (keyLiteralRef.rec(bf).contains(JsNull)) Right(None) else {
        codec.deserialize(bf).right.map(Some(_))
      }
    override def serialize(t: Option[T]): JsonVal = codec.serialize(t)
  }
}
