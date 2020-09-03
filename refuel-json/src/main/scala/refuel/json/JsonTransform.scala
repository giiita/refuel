package refuel.json

import refuel.json.codecs.Write
import refuel.json.logging.{JsonConvertLogEnabled, JsonLoggingStrategy}
import refuel.json.tokenize.decoded.DecodedJsonRaw
import refuel.json.tokenize.strategy.JsonEntry

/**
  * Context that performs Json serialize / deserialize by refuel json.
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

    def toJString[X >: T](implicit ct: Write[X]): String = {
      val buf = new StringBuffer()
      toJson[X].encode(buf)
      buf.toString
    }
  }
}
