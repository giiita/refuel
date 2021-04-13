package refuel.json.codecs.builder

import refuel.json.JsonVal

class EitherCodecConditionBuilder[L, R, C[_]](v: (JsonVal => Boolean) => C[Either[L, R]]) {
  def cond(f: JsonVal => Boolean): C[Either[L, R]] = v(f)
}
