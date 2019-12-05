package refuel.json.codecs.builder.context

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.builder.CBuildComp
import refuel.json.{Codec, Json}

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
case class JsKeyLit(v: Seq[String]) extends JsKeyLitOps {
  self =>
  /**
    * Set the key literal to add and rebuild.
    *
    * @param add Next key literal to add
    * @return
    */
  def /(add: String): JsKeyLit = JsKeyLit(v :+ add)

  /**
    * Generate a Codec to serialize / deserialize this constructed literal.
    *
    * {{{
    *   ("root" / "next" / "terminal").parsed(CaseClassCodec.from[XXX])
    * }}}
    *
    * @tparam A Internal codec type
    * @return
    */
  def parsed[A: Codec]: CBuildComp[A] = new CBuildComp[A] {
    override private[json] val k: JsKeyLitOps = self
  }

  //  def parsed[A: Codec, B: Codec, C: Codec, D: Codec, E: Codec, F: Codec, G: Codec, H: Codec, I: Codec, J: Codec, K: Codec, L: Codec, M: Codec, N: Codec, O: Codec, P: Codec, Q: Codec, R: Codec, S: Codec, T: Codec, U: Codec, V: Codec]
  //  : CBuildComp22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = new CBuildComp22 {
  //    override private[json] val k: JsKeyLitOps = self
  //  }

  /**
    * Follow the target JsonObject recursively from the constructed key literal.
    *
    * @param x Root json object
    * @return
    */
  override def rec(x: Json): Json = {
    v.foldLeft(x)(_ named _)
  }

  /**
    * TODO: Temporary implementation
    *
    * @return
    */
  override def toString: String = v.mkString
}
