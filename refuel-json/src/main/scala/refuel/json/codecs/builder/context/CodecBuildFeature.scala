package refuel.json.codecs.builder.context

import refuel.json.JsonVal
import refuel.json.codecs.builder.DeserializeConcatenation
import refuel.json.codecs.builder.context.keylit.NatureKeyRef
import refuel.json.codecs.builder.context.translation.{
  IterableCodecTranslator,
  RootCodecTranslator,
  TupleCodecTranslator
}
import refuel.json.codecs.{Read, Write}

import scala.language.implicitConversions

trait CodecBuildFeature
    extends IterableCodecTranslator
    with TupleCodecTranslator
    with RootCodecTranslator
    with DynamicCodecGenFeature {

  /** Set the key literal to add.
    * Calling [[NatureKeyRef.->>]] from String implicitly converts it to a literal object.
    *
    * @param v initial json key literal
    * @return
    */
  protected implicit def __jsonKeyLiteralBuild(v: String): NatureKeyRef = NatureKeyRef(v)

  /** Synthesize the deserialize result.
    *
    * @param v Any values
    * @tparam T Any type
    * @return
    */
  implicit def __deserializeConcatenation[T](
      v: Read[T]
  ): DeserializeConcatenation[T] =
    new DeserializeConcatenation(v)

  implicit class __Value[V](value: V) {
    @deprecated("Use `ser` instead of `to` to avoid the possibility of function name duplication.")
    def to[T >: V](implicit wr: Write[T]): JsonVal = wr.serialize(value)

    implicit def ser[T >: V](implicit wr: Write[T]): JsonVal = wr.serialize(value)
  }
}
