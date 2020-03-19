package refuel.json.entry

import refuel.json.JsonVal
import refuel.json.error.StreamIndeterminate

import scala.reflect.ClassTag

private[refuel] abstract class JsStack[T: ClassTag] private[entry](bf: JsonVal) extends JsonVal {

  def pour(b: StringBuffer): Unit = throw StreamIndeterminate(s"Cannot to be String. JsStack is an unclosed json stream.")

  override def prettyprint: String = throw StreamIndeterminate(s"Cannot to be String. JsStack is an unclosed json stream.")

  override def named(key: String): JsonVal = throw StreamIndeterminate(s"Cannot specified name. JsStack is an unclosed json stream.")

  protected var pos = 0
  protected var stack: Array[T]

  protected def glowArray: Unit

  def ++(js: JsonVal): JsonVal

  override def isSquashable: Boolean = true
}



