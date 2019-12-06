package refuel.json.codecs.builder.context

import refuel.json.codecs.builder.context.keylit.JsKeyLit
import refuel.json.codecs.builder.context.translation.{IterableCodecTranslator, TupleCodecTranslator}
import refuel.json.internal.JsonTokenizer
import refuel.json.tokenize.ObjectTokenizer

import scala.language.implicitConversions

trait CodecBuildOps extends IterableCodecTranslator with TupleCodecTranslator {
  _: JsTokenizeOps =>

  /**
    * Set the key literal to add.
    * Calling [[JsKeyLit.parsed]] or [[JsKeyLit./]] from String implicitly converts it to a literal object.
    *
    * @param v initial json key literal
    * @return
    */
  implicit def jsonKeyLiteralBuild(v: String): JsKeyLit = JsKeyLit(Seq(v))

  override final val _jer: JsonTokenizer = ObjectTokenizer
}
