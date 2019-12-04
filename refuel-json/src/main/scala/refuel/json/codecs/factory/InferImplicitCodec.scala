package refuel.json.codecs.factory
import refuel.internal.json.CaseCodecFactory
import refuel.json.Codec

/**
  * CodecGenerator used inside CodecBuilder.
  * Unlike [[CaseClassCodec]], there is a possibility to automatically guess the implicit Codec.
  * Explicit usage can cause infinite loops, self-circulation, etc.
  *
  * The behavior when an implicit guess is not made is the same as CaseClassCodec.
  *
  * @see https://github.com/giiita/refuel/issues/51
  *
  */
object InferImplicitCodec {
  def from[T]: Codec[T] = macro CaseCodecFactory.fromInferOrCase[T]
}
