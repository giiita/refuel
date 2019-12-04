package refuel.json.codecs.builder.context
import refuel.json.codecs.builder.context.translation.{IterableCodecTranslator, TupleCodecTranslator}

import scala.language.implicitConversions

trait CodecBuildOps extends IterableCodecTranslator with TupleCodecTranslator {
  implicit def jsonKeyLiteralBuild(v: String): JsKeyLit = JsKeyLit(Seq(v))
}
