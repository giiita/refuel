package refuel.json.codecs

import refuel.internal.json.CaseCodecFactory
import refuel.json.{Codec, JsonVal}

object AutoDerive extends AutoDerive

trait AutoDerive {
  implicit def autoDerivation[T]: Codec[T] = macro CaseCodecFactory.fromInferOrCase[T]

  implicit def __as[V](v: V)(implicit c: Write[V]): JsonVal = c.serialize(v)
}
