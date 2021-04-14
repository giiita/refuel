package refuel.json

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.codecs.{Read, Write}

trait JsonVal extends Serializable {

  /** Determines that the target JSON syntax tree does not exist or is a null symbol.
    * Empty arrays and empty objects are not considered to be empty.
    *
    * @return
    */
  def isEmpty: Boolean = false

  def isNonEmpty: Boolean = true

  /**
    * Synthesize Json object.
    * If the json object is json primitive type, an exception may occur.
    *
    * @param js join json objects.
    * @return
    */
  def ++(js: JsonVal): JsonVal

  def typed[T: Read](key: JsonKeyRef): T = named(key).des

  def des[T](implicit c: Read[T]): T =
    c.deserialize(this)

  /**
    * Get a json value with a specific json key from the json object.
    * JsNull may change if that key doesn't exist.
    *
    * @param key Target json key name
    * @return
    */
  def named(key: JsonKeyRef): JsonVal = key.dig(this)

  /**
    * Detects hooked binding syntax of Json literal.
    * This detects syntax errors when joining Json objects.
    * The argument receives a delimiter that is not directly related to the construction of Json.
    *
    * @param c delimiter
    */
  private[refuel] def approvalSyntax(c: Char): Unit

  /**
    * Convert to json literal.
    *
    * @return
    */
  private[refuel] def encode(b: StringBuffer): Unit

  /**
    * Squash the Json buffer under construction.
    * This will build a complete Json object.
    *
    * @return
    */
  private[refuel] def squash: JsonVal = this

  /**
    * Indicates whether the object is squashable.
    * If squashable, the object is a Json buffer under construction.
    *
    * @return
    */
  private[refuel] def isSquashable: Boolean = false

  private[refuel] def named(key: String): JsonVal

  private[refuel] def pure: String = toString

  override def toString: String = {
    val buf = new StringBuffer()
    encode(buf)
    buf.toString
  }
}

object JsonVal {
  implicit def __inferredAsWrite[V](v: V)(implicit c: Write[V]): JsonVal = c.serialize(v)
  implicit def __inferredAsRead[V](v: JsonVal)(implicit c: Read[V]): V   = c.deserialize(v)
}
