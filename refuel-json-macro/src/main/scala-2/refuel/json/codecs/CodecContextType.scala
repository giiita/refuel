package refuel.json.codecs

trait CodecContextType

class CodecContextTypeExtension[X, +T[X] <: CodecContextType](codec: T[X]) {
  def cmap[N, A[N] >: T[N]](fn: T[X] => A[N]) = fn(codec)
}
