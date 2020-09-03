package refuel.json.tokenize.strategy

import refuel.json.JsonVal
import refuel.json.codecs.Read
import refuel.json.error.{DeserializeFailPropagation, DeserializeFailed}
import refuel.json.logging.{JsonConvertLogEnabled, JsonLoggingStrategy}

import scala.util.Try

/**
  * Deserialize JsonRawData.
  * If you want to build a Json syntax tree, call [[jsonTree]]
  * Otherwise, return deserialize result with failure in [[as(CodecClassCodec.from[XXX])]].
  *
  * There are currently three main ways to generate Codec.
  *
  *
  * 1. [[CaseClassCodec]]
  * {{{
  *     CaseClassCodec.from[XXX]
  * }}}
  * Applying class and unapply function are required to use CaseClassCodec.
  * Also, the JsonRawData key and the case class field name must exactly match. Order has no effect.
  *
  *
  * 2. [[ConstCodec]]
  * {{{
  *     ConstCodec.from("key1", "key2")(XXX.apply)(XXX.unapply)
  * }}}
  * The variable length character string that becomes the first argument corresponds to the key of Json raw data.
  * Cannot refer to class field name due to dynamic apply function.
  *
  * NOTE: In addition, due to the nature of macro, it is not possible to transfer the argument of the upper function as the argument of the macro function.
  *
  *
  * 3. Custom codec builder
  * {{{
  *     "root".parsed(
  *       {
  *         "1/4".parsed(CodecA) ++
  *           "2/4".parsed(CodecA) ++
  *           "3/4".parsed(option(CodecAA)) ++
  *           "4/4".parsed(option(CodecAA))
  *         }.apply(B.apply)(B.unapply)
  *     ).apply(C.apply)(C.unapply)
  * }}}
  * You can use DSL to combine Codec and generate new Codecs.
  */
private[refuel] abstract class JsonEntry(implicit logEnabled: JsonConvertLogEnabled) extends JsonLoggingStrategy {
  protected def generateStrategy: JTransformStrategy

  def as[E](implicit c: Read[E]): Either[DeserializeFailed, E] = {
    Try {
      val js = jsonTree
      jsonReadLogging(jsonTree).des[E]
    }.toEither.left
      .map(DeserializeFailPropagation(s"Internal structure analysis by ${c.getClass} raised an exception.", _))
  }

  def jsonTree: JsonVal = generateStrategy.buildAST
}
