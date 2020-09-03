package refuel.json

import refuel.json.logging.JsonConvertLogEnabled
import refuel.json.tokenize.encoded.EncodedJsonRaw
import refuel.json.tokenize.strategy.JsonEntry

/**
  * Deserialization always performs a JSON DECODE.
  * For example, a JSON ENCODE with the line break "\\n" is recognized as the line break character '\n'.
  *
  * Serialization always enforces JSON ENCODE.
  * For example, if a JSON string contains a line break, the serialized result will always be a "\\n".
  *
  * If you don't need JSON DECODE, use [[JsonTransform]].
  *
  * When used with an HTTP Server or Client, the communication target may not support JSON ENCODE / DECODE.
  * In this case, it is safer to use EncodedJsonTransform. However, in such cases, if you want to input a backslash
  * as a string, the behavior is not intended, so if this is a problem, check the destination's response/reception format
  * and use an appropriate Transformer.
  */
trait EncodedJsonTranform extends JsonTransform {
  override protected implicit def __toEntryMaterialization(raw: String)(
    implicit logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
  ): JsonEntry = new EncodedJsonRaw(raw)
}
