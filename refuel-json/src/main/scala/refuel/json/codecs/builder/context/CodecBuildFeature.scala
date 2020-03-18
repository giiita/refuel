package refuel.json.codecs.builder.context

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.codecs.builder.context.keylit.NatureKeyRef
import refuel.json.codecs.builder.context.translation.{IterableCodecTranslator, RootCodecTranslator, TupleCodecTranslator}

import scala.language.implicitConversions

trait CodecBuildFeature extends IterableCodecTranslator with TupleCodecTranslator with RootCodecTranslator {

  /**
   * Set the key literal to add.
   * Calling [[NatureKeyRef.->]] from String implicitly converts it to a literal object.
   *
   * @param v initial json key literal
   * @return
   */
  implicit def jsonKeyLiteralBuild(v: String): NatureKeyRef = NatureKeyRef(v)
}
