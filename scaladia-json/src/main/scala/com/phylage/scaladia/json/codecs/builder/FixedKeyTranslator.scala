package com.phylage.scaladia.json.codecs.builder

import com.phylage.scaladia.json.Codec

trait FixedKeyTranslator extends CustomCodecTranslator {
  def @@(value: Codec[_]): CustomCodecTranslator
}
