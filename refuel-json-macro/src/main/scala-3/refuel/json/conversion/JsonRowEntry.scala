package refuel.json.conversion

import refuel.json.JsonVal
import refuel.json.codecs.{Read, Write}
import refuel.json.exception.InvalidDeserializationException

import scala.util.{Failure, Try}

/**
  * Deserialize JsonRawData.
  * If you want to build a Json syntax tree, call [[json]]
  * Otherwise, return deserialize result with failure in [[readAs]].
  *
  * There are currently three main ways to generate Codec.
  *
  * 1. [[CaseClass]]
  * {{{
  *     CodecDef.CaseClass[XXX]
  * }}}
  * Applying class and unapply function are required to use CaseClassCodec.
  * Also, the JsonRawData key and the case class field name must exactly match. Order has no effect.
  *
  *
  * 2. [[ConstCodec]]
  * {{{
  *     CodecDef.Construct("key1", "key2")(XXX.apply)(XXX.unapply)
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
trait JsonRowEntry {
  inline def readAs[E](using c: Read[E]): Try[E] = {
    Try {
      c.deserialize(json)
    }.recoverWith {
      case x =>
        Failure(InvalidDeserializationException(s"Failed to deserialize Json with ${c.getClass}.", x))
    }
  }

  def json: JsonVal
}
