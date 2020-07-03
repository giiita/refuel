package refuel.json.codecs

import refuel.internal.json.CaseCodecFactory
import refuel.json.Codec

object AutoDerive {
  implicit def autoDerivation[T]: Codec[T] = macro CaseCodecFactory.fromCaseClass[T]
}
