package refuel.json.codecs.builder.context

import refuel.json.internal.JsonTokenizer

trait JsTokenizeOps {
  protected implicit val _jer: JsonTokenizer
}
