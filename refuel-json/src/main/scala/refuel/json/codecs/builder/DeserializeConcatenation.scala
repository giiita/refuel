package refuel.json.codecs.builder

class DeserializeConcatenation[T](v: T) {
  def pipe[R](fn: T => R): R = fn(v)
}
