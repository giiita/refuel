package refuel.json.codecs.builder.context.translation

import refuel.json.Codec
import refuel.json.codecs.{Read, Write}
import refuel.json.codecs.builder.CBuildComp
import refuel.json.codecs.builder.context.keylit.SelfKeyRef

import scala.language.implicitConversions

trait RootCodecTranslator {
  protected implicit def __toRootCodecBuilder[Z](codec: Codec[Z]): CBuildComp[Z] = {
    SelfKeyRef.parsed(codec)
  }
  protected implicit def __toRootReadBuilder[Z](codec: Read[Z]): CBuildComp[Z] = {
    SelfKeyRef.parsed(codec.raise)
  }
  protected implicit def __toRootWriteBuilder[Z](codec: Write[Z]): CBuildComp[Z] = {
    SelfKeyRef.parsed(codec.raise)
  }

  protected implicit def __toRootCodecBuilder[Z](codec: CBuildComp[Z]): Codec[Z] = {
    codec.apply(x => x)(x => Some(x))
  }
}
