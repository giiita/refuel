package refuel.json.tokenize.decoded

import refuel.json.logging.JsonConvertLogEnabled
import refuel.json.tokenize.strategy.{JTransformStrategy, JsonEntry}

class DecodedJsonRaw private[refuel] (literal: String)(
    implicit logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
) extends JsonEntry {
  override def generateStrategy: JTransformStrategy = new DecodedJsonTokenizer(literal.trim.toCharArray)
}
