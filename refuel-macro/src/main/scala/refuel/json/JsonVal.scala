package refuel.json

trait JsonVal extends Serializable {

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
  def pour(b: StringBuffer): Unit

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
  def ++(js: JsonVal): JsonVal

  /**
   * Indicates that it is a joinable Json object.
   *
   * @return
   */
  def isIndependent: Boolean = false

  def to[T](implicit c: Codec[T]): T =
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