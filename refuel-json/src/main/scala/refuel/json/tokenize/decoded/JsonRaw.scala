package refuel.json.tokenize.decoded

import refuel.json.JsonVal
import refuel.json.conversion.JsonRowEntry
import refuel.json.tokenize.strategy.JTransformStrategy

class JsonRaw private[refuel] (literal: String) extends JsonRowEntry {
  def generateStrategy: JTransformStrategy = new DecodedJsonTokenizer(literal.trim.toCharArray)

  def json: JsonVal = generateStrategy.buildAST
}
