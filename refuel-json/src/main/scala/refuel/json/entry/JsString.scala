package refuel.json.entry
import refuel.internal.json.codec.builder.JsKeyLitOps

case class JsString private[entry] (literal: String) extends JsLiteral {}

object JsString {
  def apply(literal: JsKeyLitOps): JsString = new JsString(literal.toString)
}
