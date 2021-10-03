package refuel.json

import refuel.json.JsonVal

trait JsonKeySpec {

  /**
    * Adds a key to the end of the reference key.
    * "a" @@ "b" is "b" in "a"
    *
    * @param v Additional key literal
    * @return
    */
  def @@(v: String): JsonKeySpec

  /**
    * Mining Json object with Json key that can be layered.
    *
    * @param v Mined json object.
    * @return
    */
  def dig(v: JsonVal): JsonVal

  /**
    * Create wrapped entry.
    *
    * @param v Json value
    * @return
    */
  def apply(v: JsonVal): JsonVal
}
