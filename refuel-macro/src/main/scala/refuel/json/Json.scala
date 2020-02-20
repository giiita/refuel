package refuel.json

import refuel.json.error.DeserializeFailed

trait Json extends Serializable {
  /**
   * Convert to json literal.
   *
   * @return
   */
  def toString: String

  /**
   * Unlike toString, it doesn't wrap in quotations according to json rules.
   * Returns only pure json value.
   *
   * @return
   */
  def unquote: String = toString

  /**
   * Outputs a highly visible string including blanks and newlines.
   *
   * @return
   */
  def prettyprint: String = toString

  /**
   * Synthesize Json object.
   * If the json object is json primitive type, an exception may occur.
   *
   * @param js join json objects.
   * @return
   */
  def ++(js: Json): Json

  def isIndependent: Boolean = false

  def to[T](implicit c: Codec[T]): Either[DeserializeFailed, T] =
    c.deserialize(this)

  /**
   * Get a json value with a specific json key from the json object.
   * JsNull may change if that key doesn't exist.
   *
   * @param key Target json key name
   * @return
   */
  def named(key: String): Json


  def squash: Json = this
  def isSquashable: Boolean = false
}