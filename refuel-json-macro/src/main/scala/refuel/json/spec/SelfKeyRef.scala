package refuel.json.spec

import refuel.json.{JsonKeySpec, JsonVal}

/**
  * Json key literal builder.
  * Used to synthesize Codec.
  *
  * Build practices.
  * {{{
  *   ("root" @@ "next" @@ "terminal")
  * }}}
  */
case object SelfKeyRef extends JsonKeySpec {

  /**
    * Mining Json object with Json key that can be layered.
    *
    * @param v Mined json object.
    * @return
    */
  override def dig(v: JsonVal): JsonVal = v

  /**
    * Tuple conversion syntax suger.
    *
    * @param v Json value
    * @return
    */
  override def apply(v: JsonVal): JsonVal = v

  /**
    * Adds a key to the end of the reference key.
    *
    * @param v Additional key literal
    * @return
    */
  override def @@(v: String): JsonKeyStructure = JsonKeyStructure(v)
}
