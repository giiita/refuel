package refuel.json.codecs.builder.context

import refuel.internal.json.codec.builder.JsonKeyRef
import refuel.json.codecs.{CodecRaiseable, CodecTyper, Read, Write}
import refuel.json.{Codec, JsonVal}

trait DynamicCodecGenFeature {
  def Serialize[T](f: T => JsonVal): Write[T]                     = f(_)
  def Deserialize[T](f: JsonVal => T): Read[T]                    = f(_)
  def Format[T](apl: JsonVal => T)(unapl: T => JsonVal): Codec[T] = apl -> unapl

  def Wrap[T, C[_] <: CodecRaiseable[_]](key: JsonKeyRef)(implicit codec: C[T], mapper: CodecTyper[C]): C[T] =
    mapper.wrap(key)(codec)

}

object DynamicCodecGenFeature extends CodecDefinitionContext
