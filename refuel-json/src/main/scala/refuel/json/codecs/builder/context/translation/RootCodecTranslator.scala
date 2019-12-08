package refuel.json.codecs.builder.context.translation

import refuel.json.Codec
import refuel.json.codecs.builder.CBuildComp
import refuel.json.codecs.builder.context.keylit.SelfCirculationLit

trait RootCodecTranslator {
  protected implicit def toRootCodecBuilder[Z](codec: Codec[Z]): CBuildComp[Z] = {
    SelfCirculationLit.parsed(codec)
  }
}
