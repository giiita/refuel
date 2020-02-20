package refuel.json.codecs.builder.context

import refuel.json.codecs.builder.context.keylit.JsKeyLit
import refuel.json.codecs.builder.context.translation.{IterableCodecTranslator, RootCodecTranslator, TupleCodecTranslator}

import scala.language.implicitConversions

trait CodecBuildOps extends IterableCodecTranslator with TupleCodecTranslator with RootCodecTranslator {

  /**
   * Set the key literal to add.
   * Calling [[JsKeyLit.parsed]] or [[JsKeyLit./]] from String implicitly converts it to a literal object.
   *
   * @param v initial json key literal
   * @return
   */
  implicit def jsonKeyLiteralBuild(v: String): JsKeyLit = JsKeyLit(Seq(v))
}
