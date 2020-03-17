package refuel.json.tokenize

import refuel.json.JsonVal
import refuel.json.entry.JsEmpty

class JTransformRouter(rss: String) extends JTokenizer(rss.trim().toCharArray) {

  def jsonTree: JsonVal = {
    pos = 0
    if (pos >= length) beEOF
    loop(JsEmpty)
  }
}
