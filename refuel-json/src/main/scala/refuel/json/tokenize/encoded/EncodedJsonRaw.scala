package refuel.json.tokenize.encoded

import refuel.json.logging.JsonConvertLogEnabled
import refuel.json.tokenize.strategy.{JTransformStrategy, JsonEntry}

class EncodedJsonRaw private[refuel] (literal: String)(
    implicit logEnabled: JsonConvertLogEnabled = JsonConvertLogEnabled.Default
) extends JsonEntry {
  override def generateStrategy: JTransformStrategy = new EncodedJsonTokenizer(literal.trim.toCharArray)
}
