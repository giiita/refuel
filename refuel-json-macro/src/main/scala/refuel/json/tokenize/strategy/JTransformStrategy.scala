package refuel.json.tokenize.strategy

import refuel.json.JsonVal
import refuel.json.JsonVal.JsEmpty
import refuel.json.tokenize.Types.ResultBuff
import refuel.json.tokenize.util.ExtensibleIndexIncrementation

abstract class JTransformStrategy(rs: Array[Char]) extends ExtensibleIndexIncrementation(rs) {

  def buildAST: JsonVal = {
    if (pos >= length) JsEmpty else tokenize(JsEmpty)
  }

  protected def tokenize(buffer: ResultBuff[JsonVal]): JsonVal
}
