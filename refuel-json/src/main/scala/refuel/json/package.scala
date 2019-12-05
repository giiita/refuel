package refuel

import refuel.json.internal.JsonTokenizer
import refuel.json.tokenize.ObjectTokenizer

package object json {
  implicit val tokenizerRoot: JsonTokenizer = ObjectTokenizer
}
