package refuel.json.codecs.builder.context

import refuel.json.JsonVal
import refuel.json.codecs.builder.context.keylit.NatureKeyRef
import refuel.json.codecs.builder.context.translation.{
  IterableCodecTranslator,
  RootCodecTranslator,
  TupleCodecTranslator
}
import refuel.json.codecs.builder.context.write.DynamicCodecGenFeature
import refuel.json.codecs.{AutoDerive, Write}

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

  protected def as[V](v: V)(implicit c: Write[V]): JsonVal = AutoDerive.__as(v)
}
