package refuel.json.codecs

import refuel.internal.json.CaseCodecFactory
import refuel.json.{Codec, JsonVal}

object AutoDerive extends AutoDerive

trait AutoDerive {
  implicit def autoDerivation[T]: Codec[T] = macro CaseCodecFactory.fromInferOrCase[T]

  implicit def __as[V, C <: Write[V]](v: V)(implicit c: C): JsonVal = c.serialize(v)
//  implicit def __as[V](v: V)(implicit c: Codec[V]): JsonVal = c.serialize(v)

  implicit def __to[V](v: JsonVal)(implicit c: Read[V]): V  = c.deserialize(v)
  implicit def __to[V](v: JsonVal)(implicit c: Codec[V]): V = c.deserialize(v)
}
