package refuel.json.codecs.builder.context.write

import refuel.json.{Codec, JsonVal}

trait DynamicCodecGenFeature {
  def Serialize[T](f: T => JsonVal): Codec[T] = f
  def Deserialize[T](f: JsonVal => T): Codec[T] = f
  def Format[T](apl: JsonVal => T)(unapl: T => JsonVal): Codec[T] = apl -> unapl
}
