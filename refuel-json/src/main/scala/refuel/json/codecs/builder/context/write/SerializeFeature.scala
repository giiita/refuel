package refuel.json.codecs.builder.context.write

import refuel.json.{Codec, JsonVal}

trait SerializeFeature {
  def Serialize[T](f: T => JsonVal): Codec[T] = f
  def Deserialize[T](f: JsonVal => T): Codec[T] = f
}
