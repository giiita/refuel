package refuel.internal.json.codec.builder

import refuel.json.JsonVal

trait JsonKeyRef {

  /**
   * Adds a key to the end of the reference key.
   *
   * @param v Additional key literal
   * @return
   */
  def /(v: String): JsonKeyRef

  /**
   * Mining Json object with Json key that can be layered.
   *
   * @param v Mined json object.
   * @return
   */
  def dig(v: JsonVal): JsonVal

  /**
   * Tuple conversion syntax suger.
   *
   * @param v Json value
   * @return
   */
  def ->>(v: JsonVal): JsonVal
}