package refuel.json

import refuel.json.codecs.All
import refuel.json.codecs.builder.context.CodecBuildFeature

trait CodecDef extends All with CodecBuildFeature {
  protected final val CaseClassCodec = refuel.json.codecs.factory.CaseClassCodec
  protected final val ConstCodec = refuel.json.codecs.factory.ConstCodec
}
