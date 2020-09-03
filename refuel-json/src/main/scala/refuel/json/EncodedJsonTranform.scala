package refuel.json

import refuel.json.logging.JsonConvertLogEnabled
import refuel.json.tokenize.encoded.EncodedJsonRaw
import refuel.json.tokenize.strategy.JsonEntry

/**
  * Context that performs Json serialize / deserialize by refuel json.
  */
trait EncodedJsonTranform extends JsonTransform {
  override protected implicit def __toEntryMaterialization(raw: String)(
      implicit logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
  ): JsonEntry = new EncodedJsonRaw(raw)
}
