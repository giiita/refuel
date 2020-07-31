package refuel.json.codecs

import refuel.json.codecs.builder.context.CodecBuildFeature
import refuel.json.{Codec, JsonVal}
import refuel.json.codecs.definition.{AnyRefCodecsExport, AnyValCodecs, TupleCodecsImpl}

private[json] trait All extends AnyValCodecs with AnyRefCodecsExport with TupleCodecsImpl { _: CodecBuildFeature =>
  implicit val JsonCodec: Codec[JsonVal] = Format(x => x)(x => x)
}
