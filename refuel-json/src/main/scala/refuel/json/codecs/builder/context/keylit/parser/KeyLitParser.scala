package refuel.json.codecs.builder.context.keylit.parser

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.Codec
import refuel.json.codecs.builder.CBuildComp

trait KeyLitParser extends JsonKeyRef {
  self =>
  /**
   * Generate a Codec to serialize / deserialize this constructed literal.
   *
   * {{{
   *   (
   *     ("depth1" / "depth2" / "depth3") -> CaseClassCodec.from[XXX] ++
   *       ("depth1" / "depth2" / "depth4") -> CaseClassCodec.from[YYY]
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
    override private[json] val k: JsonKeyRef = self
  }

  def apply[A: Codec]: Codec[A] = parsed[A].apply(x => x)(x => Some(x))
}
