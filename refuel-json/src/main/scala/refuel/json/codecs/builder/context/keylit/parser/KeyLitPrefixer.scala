package refuel.json.codecs.builder.context.keylit.parser

import refuel.internal.json.codec.builder.JsKeyLitOps
import refuel.json.codecs.builder.context.keylit.JsKeyLit
import refuel.json.error.{DeserializeFailPropagation, DeserializeFailed}
import refuel.json.{Codec, Json}

trait KeyLitPrefixer {
  self: JsKeyLit =>

  /**
   * Unlike [[KeyLitParser.parsed]], the behavior of serialize / deserialize is not changed.
   * Only the insertion path and acquisition path are changed when building the syntax tree.
   *
   * {{{
   *   ("depth1" / "depth2").extend(
   *     "depth3".parsed(codec)(XXX.apply)(XXX.unapply)
   *   )
   * }}}
   *
   * be equal
   *
   * {{{
   *   {
   *     "depth1": {
   *       "depth2": {
   *         "depth3": "xxx"
   *       }
   *     }
   *   }
   * }}}
   *
   * @param v internal codec.
   * @tparam A parsed type of codec.
   * @return
   */
  def extend[A](implicit v: Codec[A]): Codec[A] = new Codec[A] {
    override def deserialize(bf: Json): Either[DeserializeFailed, A] = {
      v.deserialize(keyLiteralRef.rec(bf).head)
    }

    override def keyLiteralRef: JsKeyLitOps = v.keyLiteralRef prefix self.v

    override def serialize(t: A): Json = {
      keyLiteralRef.additionalKeyRef(Seq(v.serialize(t)))
    }
  }
}
