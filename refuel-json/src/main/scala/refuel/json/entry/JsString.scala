package refuel.json.entry

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.JsonVal
import refuel.json.error.{CannotAccessJsonKey, IllegalJsonSyntaxTreeBuilding}

import scala.annotation.{switch, tailrec}

case class JsString private (literal: String) extends JsVariable {

  override def pure: String = literal

  private[this] final def toEscaping(c: Char, b: StringBuffer): Unit = {
    (c: @switch) match {
      case '\\'  => b.append("\\\\")
      case '"'   => b.append("\\\"")
      case '\r'  => b.append("\\r")
      case '\n'  => b.append("\\n")
      case '\f'  => b.append("\\f")
      case '\b'  => b.append("\\b")
      case '\t'  => b.append("\\t")
      case other => b.append(other)
    }
  }

  def encode(b: StringBuffer): Unit = {
    b.append('"')
    var processIndex = 0
    val maxIndex     = literal.length - 1

    @tailrec
    def detect(i: Int): Unit = {
      if (i <= maxIndex) {
        toEscaping(literal(processIndex), b)
        processIndex += 1
        detect(i + 1)
      }
    }

    detect(0)
    b.append('"')
  }

  override def ++(js: JsonVal): JsonVal = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsLiteral.")

  override def named(key: String): JsonVal = throw CannotAccessJsonKey(s"Cannot access key : $key of $toString")
}

object JsString {
  def apply(literal: String): JsString     = new JsString(literal)
  def apply(literal: JsonKeyRef): JsString = new JsString(literal.toString)
}
