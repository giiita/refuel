package refuel.json

import refuel.json.codecs.{Read, Write}

trait JsonVal extends Serializable {

  private[refuel] def pure: String = toString

  override def toString: String = {
    val buf = new StringBuffer()
    writeToBufferString(buf)
    buf.toString
  }

  def writeToBufferString(buffer: StringBuffer): Unit

  /** Determines that the target JSON syntax tree does not exist or is a null symbol.
    * Empty arrays and empty objects are not considered to be empty.
    *
    * @return
    */
  def isEmpty: Boolean     = false
  def isNonEmptry: Boolean = true

  /**
    * Detects hooked binding syntax of Json literal.
    * This detects syntax errors when joining Json objects.
    * The argument receives a delimiter that is not directly related to the construction of Json.
    *
    * @param c delimiter
    */
  def approvalSyntax(c: Char): Unit

  /**
    * Convert to json literal.
    *
    * @return
    */
  def encode(b: StringBuffer): Unit

  /**
    * Synthesize Json object.
    * If the json object is json primitive type, an exception may occur.
    *
    * @param js join json objects.
    * @return
    */
  def ++(js: JsonVal): JsonVal

  @deprecated("Use `des` instead of `to` to avoid the possibility of function name duplication.")
  def to[T](implicit c: Read[T]): T =
    c.deserialize(this)

  def des[T](implicit c: Read[T]): T =
    c.deserialize(this)

  /**
    * Get a json value with a specific json key from the json object.
    * JsNull may change if that key doesn't exist.
    *
    * @param key Target json key name
    * @return
    */
  def named(key: String): JsonVal

  /**
    * Squash the Json buffer under construction.
    * This will build a complete Json object.
    *
    * @return
    */
  def squash: JsonVal = this

  /**
    * Indicates whether the object is squashable.
    * If squashable, the object is a Json buffer under construction.
    *
    * @return
    */
  def isSquashable: Boolean = false
}

object JsonVal {
  implicit def __inferredAsWrite[V](v: V)(implicit c: Write[V]): JsonVal = c.serialize(v)
}
