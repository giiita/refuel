package refuel.json.codecs.builder.context.write

import refuel.json.codecs.{Read, Write}
import refuel.json.{Codec, JsonVal}

trait DynamicCodecGenFeature {
  def Serialize[T](f: T => JsonVal): Write[T] = f(_)
  def Deserialize[T](f: JsonVal => T): Read[T] = f(_)
  def Format[T](apl: JsonVal => T)(unapl: T => JsonVal): Codec[T] = apl -> unapl
}
