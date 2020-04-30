package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.JsonVal
import refuel.json.codecs.builder.context.keylit.parser.KeyLitParser

/**
  * Json key literal builder.
  * Used to synthesize Codec.
  *
  * Build practices.
  * {{{
  *   ("root" / "next" / "terminal")
  * }}}
  */
case object SelfKeyRef extends JsonKeyRef with KeyLitParser {

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
  override def ->>(v: JsonVal): JsonVal = v

  /**
    * Adds a key to the end of the reference key.
    *
    * @param v Additional key literal
    * @return
    */
  override def @@(v: String): NatureKeyRef = NatureKeyRef(v)
}
