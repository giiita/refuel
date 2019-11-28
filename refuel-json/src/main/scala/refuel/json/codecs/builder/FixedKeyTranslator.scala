package refuel.json.codecs.builder

import refuel.json.Codec

trait FixedKeyTranslator extends CustomCodecTranslator {
  def @@(value: Codec[_]): CustomCodecTranslator
}
