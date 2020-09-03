package refuel.json

import refuel.json.codecs.Write
import refuel.json.logging.{JsonConvertLogEnabled, JsonLoggingStrategy}
import refuel.json.tokenize.decoded.DecodedJsonRaw
import refuel.json.tokenize.strategy.JsonEntry

/**
  * Deserialization always builds an AST equivalent to the input and does not do any JSON DECODE of the escape character.
  *
  * Serialization always enforces JSON ENCODE.
  * For example, if a JSON string contains a line break, the serialized result will always be a "\\n".
  *
  * If you need JSON DECODE, use [[EncodedJsonTransform]].
  *
  * When used with an HTTP Server or Client, the communication target may not support JSON ENCODE / DECODE.
  * In this case, it is safer to use EncodedJsonTransform. However, in such cases, if you want to input a backslash
  * as a string, the behavior is not intended, so if this is a problem, check the destination's response/reception format
  * and use an appropriate Transformer.
  */
trait JsonTransform extends JsonLoggingStrategy {
  protected implicit def __toEntryMaterialization(raw: String)(
      implicit logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
  ): JsonEntry = new DecodedJsonRaw(raw)

  /**
    * Serialize any object to Json syntax tree.
    * In this state, it is not JsonRawData, but it becomes JsonRawData by [[JsonVal.encode]].
    * A function that takes an implicit codec, but in many cases it will require explicit assignment.
    * {{{
    *   ???.toJson(CaseClassCodec.from[XXX])
    * }}}
    *
    * @param t Any object
    * @tparam T Any object type
    */
  protected implicit class JSerialization[T](t: T)(
      implicit logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
  ) {
    def toJson[X >: T](implicit ct: Write[X]): JsonVal = {
      jsonWriteLogging(ct.serialize(t))
    }

    /** Use the `toJString` to perform JSON Serialize. At this time, the output JSON format string is always JSON ENCODE.
      * For example, if a JSON string contains a new line, the output is always the string "\\n".
      *
      * The toString operation will also output a JSON-style string, but it will not be JSON ENCODE.
      * For example, if you do a Json.str("foo\n\"bar"), you will get `"foo foo\\\\n\"bar"` by toJString, but you will get `"foo\n\"bar"` bar by toString.
      *
      * Also, as[String] returns the same result as toString.
      *
      * @param ct
      * @tparam X
      * @return
      * */
    def toJString[X >: T](implicit ct: Write[X]): String = {
      val buf = new StringBuffer()
      toJson[X].encode(buf)
      buf.toString
    }
  }

}
