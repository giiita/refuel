package refuel.json.codecs.builder

import refuel.json.JsonVal
import refuel.json.codecs.CodecRaiseable

class EitherCodecConditionBuilder[L, R, C[_] <: CodecRaiseable[_]](v: (JsonVal => Boolean) => C[Either[L, R]]) {
  def cond(f: JsonVal => Boolean): C[Either[L, R]] = v(f)
}
