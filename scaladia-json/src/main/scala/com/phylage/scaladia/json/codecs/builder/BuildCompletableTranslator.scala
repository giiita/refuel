package com.phylage.scaladia.json.codecs.builder

import com.phylage.scaladia.json.Codec

trait BuildCompletableTranslator extends CustomCodecTranslator {
  def complete[Z]: Codec[Z]
}
