package refuel.json.tokenize

import refuel.json.Json
import refuel.json.entry.JsEmpty

class JTransformRouter(rss: String) extends JTokenizer(rss.trim().toCharArray) {

  def jsonTree: Json = {
    pos = 0
    if (pos >= length) throwUnexpectedEOF
    loop(JsEmpty)
  }
}
