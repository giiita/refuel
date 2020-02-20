package refuel.json.entry

import java.util

import refuel.json.Json
import refuel.json.error.StreamIndeterminate

import scala.reflect.ClassTag

abstract class JsStack[T: ClassTag] private[entry](bf: Json) extends Json {

  override def toString: String = throw StreamIndeterminate(s"Cannot to be String. JsStack is an unclosed json stream.")

  override def prettyprint: String = throw StreamIndeterminate(s"Cannot to be String. JsStack is an unclosed json stream.")

  override def named(key: String): Json = throw StreamIndeterminate(s"Cannot specified name. JsStack is an unclosed json stream.")

  protected var pos = 0
  protected var stack: Array[T]

  protected def glowArray: Unit

  def ++(js: Json): Json

  override def isSquashable: Boolean = true
}



