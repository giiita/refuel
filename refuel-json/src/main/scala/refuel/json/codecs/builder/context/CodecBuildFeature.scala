package refuel.json.codecs.builder.context

import refuel.internal.json.CaseCodecFactory
import refuel.json.codecs.builder.context.keylit.NatureKeyRef
import refuel.json.codecs.builder.context.translation.{
  IterableCodecTranslator,
  RootCodecTranslator,
  TupleCodecTranslator
}
import refuel.json.codecs.builder.context.write.DynamicCodecGenFeature
import refuel.json.codecs.{Read, Write}
import refuel.json.{Codec, JsonVal}

import scala.language.implicitConversions

trait CodecBuildFeature
    extends IterableCodecTranslator
    with TupleCodecTranslator
    with RootCodecTranslator
    with DynamicCodecGenFeature {

  /**
    * Set the key literal to add.
    * Calling [[NatureKeyRef.->>]] from String implicitly converts it to a literal object.
    *
    * @param v initial json key literal
    * @return
    */
  protected implicit def __jsonKeyLiteralBuild(v: String): NatureKeyRef = NatureKeyRef(v)

  protected implicit def __serializeToConst[T](v: Codec[T]): T => JsonVal = v.serialize
  protected implicit def __serializeToConst[T](v: Write[T]): T => JsonVal = v.serialize

  protected implicit def __deserializeToConst[T](v: Codec[T]): JsonVal => T = v.deserialize
  protected implicit def __deserializeToConst[T](v: Read[T]): JsonVal => T  = v.deserialize

  protected implicit def __jsonBuildCriteria[V](v: V)(implicit c: Codec[V]): JsonVal = c.serialize(v)
}
