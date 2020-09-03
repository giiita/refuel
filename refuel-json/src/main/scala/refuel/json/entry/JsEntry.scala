package refuel.json.entry

import refuel.json.JsonVal
import refuel.json.error.UnsupportedOperation

/**
  * JSON Object being constructed.
  *
  * @param key JSON key
  * @param value JSON value
  */
case class JsEntry(key: JsString, value: JsonVal) extends JsonVal {

  override def toString: String = JsObject.fromEntry(this).toString

  private[refuel] def asTuple: (JsString, JsonVal) = key -> value

  /**
    * Detects hooked binding syntax of Json literal.
    * This detects syntax errors when joining Json objects.
    * The argument receives a delimiter that is not directly related to the construction of Json.
    *
    * @param c delimiter
    */
  override def approvalSyntax(c: Char): Unit =
    throw UnsupportedOperation("Operations other than composition are not allowed for incomplete Json entries.")

  /**
    * Convert to json literal.
    *
    * @return
    */
  override def encode(b: StringBuffer): Unit =
    throw UnsupportedOperation("Operations other than composition are not allowed for incomplete Json entries.")

  /**
    * Synthesize Json object.
    * If the json object is json primitive type, an exception may occur.
    *
    * @param js join json objects.
    * @return
    */
  override def ++(js: JsonVal): JsonVal =
    throw UnsupportedOperation("Operations other than composition are not allowed for incomplete Json entries.")

  /**
    * Get a json value with a specific json key from the json object.
    * JsNull may change if that key doesn't exist.
    *
    * @param _key Target json key name
    * @return
    */
  override def named(_key: String): JsonVal =
    if (key.pure == _key) {
      value
    } else JsNull

  override def writeToBufferString(buffer: StringBuffer): Unit = JsObject.fromEntry(this).writeToBufferString(buffer)
}
