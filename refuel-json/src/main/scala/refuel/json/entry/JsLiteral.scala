package refuel.json.entry

import refuel.json.Json
import refuel.json.error.{CannotAccessJsonKey, IllegalJsonSyntaxTreeBuilding}

trait JsLiteral extends JsVariable with CharSequence {
  private[entry] val literal: String

  override def toString: String = s""""${literal.map {
    case '\\' => "\\\\"
    case '"' => "\\\""
    case x => x
  }.mkString}""""

  override def unquote: String = literal

  override def length(): Int = literal.length()

  override def charAt(i: Int): Char = literal.charAt(i)

  override def subSequence(i: Int, i1: Int): CharSequence = literal.subSequence(i, i1)

  def toKey(jso: JsObject): JsKeyBuffer = new JsKeyBuffer(this, jso)

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsLiteral.")

  override def named(key: String): Json = throw CannotAccessJsonKey(s"Cannot access key : $key of $toString")
}
