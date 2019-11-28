package refuel.json.codecs.builder

import refuel.json.Codec

trait BuildCompletableTranslator extends CustomCodecTranslator {
  def complete[Z]: Codec[Z]
}
