package refuel.json.spec

import refuel.json.{Json, JsonKeySpec, JsonVal}

/**
  * Json key literal builder.
  * Used to synthesize Codec.
  *
  * Build practices.
  * {{{
  *   ("root" @@ "next" @@ "terminal")
  * }}}
  *
  * @param refs json key literal set
  */
case class JsonKeyStructure(refs: String*) extends JsonKeySpec {

  /**
    * Mining Json object with Json key that can be layered.
    *
    * @param v Mined json object.
    * @return
    */
  override def dig(v: JsonVal): JsonVal = refs.foldLeft(v) {
    case (r, literal) => r.named(literal)
  }

  /**
    * Tuple conversion syntax suger.
    *
    * @param v Json value
    * @return
    */
  override def apply(v: JsonVal): JsonVal = refs.foldRight(v) {
    case (literal, res) => Json.obj(literal -> res)
  }

  /**
    * Adds a key to the end of the reference key.
    *
    * @param v Additional key literal
    * @return
    */
  override def @@(v: String): JsonKeyStructure = JsonKeyStructure(refs :+ v: _*)
}
