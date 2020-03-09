package refuel.json.entry

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Json
import refuel.json.error.{CannotAccessJsonKey, IllegalJsonSyntaxTreeBuilding}

import scala.annotation.tailrec

case class JsString(literal: String) extends JsVariable {

  override def toString: String = literal

  def pour(b: StringBuffer): Unit = {
    b.append('"')
    var processIndex = 0
    val maxIndex = literal.length - 1

    @tailrec
    def detect(i: Int): Unit = {
      if (i <= maxIndex) {
        val x = literal(processIndex)
        if (x == '"' || x == '\\') {
          b.append('\\')
        }
        b.append(x)
        processIndex += 1
        detect(i + 1)
      }
    }

    detect(0)
    b.append('"')
  }

  override def ++(js: Json): Json = throw IllegalJsonSyntaxTreeBuilding("Cannot add element to JsLiteral.")

  override def named(key: String): Json = throw CannotAccessJsonKey(s"Cannot access key : $key of $toString")
}

object JsString {
  def apply(literal: JsKeyLitOps): JsString = new JsString(literal.toString)
}
