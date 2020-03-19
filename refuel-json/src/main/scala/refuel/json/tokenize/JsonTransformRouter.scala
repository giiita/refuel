package refuel.json.tokenize

import refuel.json.JsonVal
import refuel.json.entry.JsEmpty

class JsonTransformRouter(rss: String) extends JsonTokenizer(rss.trim().toCharArray) {
  def jsonTree: JsonVal = {
    pos = 0
    if (pos >= length) beEOF
    loop(JsEmpty)
  }
}
