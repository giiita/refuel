package refuel.json.codecs.builder.context.keylit.parser

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.Codec
import refuel.json.codecs.builder.CBuildComp

trait KeyLitParser extends JsKeyLitOps {
  self =>
  /**
   * Generate a Codec to serialize / deserialize this constructed literal.
   *
   * {{{
   *   (
   *     ("depth1" / "depth2" / "depth3").parsed(CaseClassCodec.from[XXX]) ++
   *       ("depth1" / "depth2" / "depth4").parsed(CaseClassCodec.from[YYY])
   *   ).apply(ZZZ.apply)(ZZZ.unapply)
   * }}}
   * be equal
   * {{{
   *   {
   *     "depth1": {
   *       "depth2": {
   *         "depth3": "xxx",
   *         "depth4": "yyy"
   *       }
   *     }
   *   }
   * }}}
   *
   * @tparam A Internal codec type
   * @return
   */
  def parsed[A: Codec]: CBuildComp[A] = new CBuildComp[A] {
    override private[json] val k: JsKeyLitOps = self
  }
}
