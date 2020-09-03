package refuel.json.tokenize.strategy

import refuel.json.JsonVal
import refuel.json.entry.JsEmpty
import refuel.json.tokenize.ResultBuff
import refuel.json.tokenize.combinator.ExtensibleIndexWhere

abstract class JTransformStrategy(rs: Array[Char]) extends ExtensibleIndexWhere(rs) {

  override var pos = 0

  def buildAST: JsonVal = {
    if (pos >= length) JsEmpty else tokenize(JsEmpty)
  }

  protected def tokenize(buffer: ResultBuff[JsonVal]): JsonVal
}
