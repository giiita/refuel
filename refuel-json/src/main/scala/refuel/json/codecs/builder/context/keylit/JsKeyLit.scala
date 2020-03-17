package refuel.json.codecs.builder.context.keylit

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.JsonVal
import refuel.json.codecs.builder.context.keylit.parser.{KeyLitParser, KeyLitPrefixer}
import refuel.json.entry.JsObject

/**
 * Json key literal builder.
 * Used to synthesize Codec.
 *
 * Build practices.
 * {{{
 *   ("root" / "next" / "terminal")
 * }}}
 *
 * @param v json key literal set
 */
case class JsKeyLit(v: Seq[String]) extends JsKeyLitOps with KeyLitParser with KeyLitPrefixer {
  self =>
  /**
   * Set the key literal to add and rebuild.
   *
   * @param add Next key literal to add
   * @return
   */
  def /(add: String): JsKeyLit = JsKeyLit(v :+ add)

  def ++(that: JsKeyLitOps): JsKeyLitOps = MultipleKeyLit(Seq(this, that))


  /**
   * Follow the target JsonObject recursively from the constructed key literal.
   *
   * @param x Root json object
   * @return
   */
  override def rec(x: JsonVal): Seq[JsonVal] = Seq {
    v.foldLeft(x)(_ named _)
  }

  def additionalKeyRef(sers: Seq[JsonVal]): JsonVal = {
    v.foldRight(sers.head)((a, b) => JsObject(a -> b))
  }

  override def prefix(that: Seq[String]): JsKeyLitOps = copy(that ++ v)
}
